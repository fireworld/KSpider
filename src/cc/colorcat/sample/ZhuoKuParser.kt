package cc.colorcat.sample

import cc.colorcat.kspider.Parser
import cc.colorcat.kspider.Scrap
import cc.colorcat.kspider.Seed
import cc.colorcat.kspider.WebSnapshot
import org.jsoup.Jsoup

/**
 * http://www.zhuoku.com/zhuomianbizhi/jing-car/20180112160924(1).htm#turn
 * Created by cxx on 2018/2/2.
 * xx.ch@outlook.com
 */
class ZhuoKuParser : Parser {
    private val zkHost = "zhuoku.com"

    override fun parse(seed: Seed, snapshot: WebSnapshot): List<Scrap> {
        if (seed.uri.host.contains(zkHost)) {
            val scraps = mutableListOf<Scrap>()
            val doc = Jsoup.parse(snapshot.contentToString(), seed.baseUrl())

            doc.select("img#imageview[src~=^(http)(s)?://(.)*\\.(jpg|png|jpeg)$]")
                    .map { it.attr("src") }
                    .mapTo(scraps) { seed.newScrapWithFill("url", it) }

            doc.select("div#bizhi a[href$=.htm]")
                    .map { it.attr("href") }
                    .mapTo(scraps) { seed.newScrapWithJoin(it) }

            return scraps
        }
        return emptyList()
    }
}
