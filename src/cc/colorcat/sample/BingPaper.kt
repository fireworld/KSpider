package cc.colorcat.sample

import cc.colorcat.kspider.*
import org.jsoup.Jsoup
import java.io.File

/**
 * Created by cxx on 2018/2/2.
 * xx.ch@outlook.com
 */
const val TAG = "image"
const val HOST = "bing.ioliu.cn"

class BingParser : Parser {
    override fun parse(seed: Seed, snapshot: WebSnapshot): List<Scrap> {
        val scraps = mutableListOf<Scrap>()
        val doc = Jsoup.parse(snapshot.contentToString(), seed.baseUrl())
        val elements = doc.select("a[class='ctrl download'][href~=^(/photo/)(.)*(force=download)$][target='_blank'][rel=nofollow]")
        elements.map { it.attr("href") }
                .map { seed.newUriWithJoin(it) }
                .mapTo(scraps) { seed.newScrapWithFill("url", it) }
        doc.select("a[href~=/(.)*\\?p=(\\d)+]").last()?.also {
            val nextSubUrl = it.attr("href")
            scraps.add(seed.newScrapWithJoin(nextSubUrl))
        }
        return scraps
    }
}

class BingHandler : Handler {
    override fun handle(scrap: Scrap): Boolean {
        val data = scrap.data
        val url = data["url"]
        if (url != null && url.matches("^(http)(s)?://(.)*(force=download)$".toRegex())) {
            val folderName = data["dir"] ?: "Bing"
            val fileName: String
            val startIndex = url.lastIndexOf('/') + 1
            val queryIndex = url.indexOf('?')
            fileName = if (queryIndex != -1) {
                url.substring(startIndex, queryIndex) + ".jpg"
            } else {
                System.nanoTime().toString() + ".jpg"
            }
            val saveDir = File("D:\\KSpider", folderName)
            if (saveDir.exists() || saveDir.mkdirs()) {
                DownloadManager.download(url, File(saveDir, fileName))
            }
            return true
        }
        return false
    }
}
