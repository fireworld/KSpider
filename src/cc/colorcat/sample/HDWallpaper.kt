package cc.colorcat.sample

import cc.colorcat.kspider.Parser
import cc.colorcat.kspider.Scrap
import cc.colorcat.kspider.Seed
import cc.colorcat.kspider.WebSnapshot
import org.jsoup.Jsoup
import java.util.*

/**
 * Created by cxx on 2018/2/2.
 * xx.ch@outlook.com
 */
const val HD_HOST = "www.hdwallpapers.in"

class HdParser : Parser {
    override fun parse(seed: Seed, snapshot: WebSnapshot): List<Scrap> {
        if (HD_HOST == seed.uri.host) {
            val scraps = LinkedList<Scrap>()
            val doc = Jsoup.parse(snapshot.contentToString(), seed.baseUrl())

            // find image's detail page
            doc.select("ul.wallpapers a[href$=.html]")
                    .map { it.attr("href") }
                    .mapTo(scraps) { seed.newScrapWithJoin(it) }

            // find next page
            doc.select("div.pagination > span.selected + a[href^=/]")
                    .map { it.attr("href") }
                    .mapTo(scraps) { seed.newScrapWithJoin(it) }

            // find image url
            doc.select("div.thumbbg1 a[href~=^(/)(.)*\\.(jpg|png|jpeg)][target=_blank]")
                    .map { it.attr("href") }
                    .mapTo(scraps) { seed.newScrapWithFill("url", seed.newUriWithJoin(it)) }

            return scraps
        }
        return emptyList()
    }
}
