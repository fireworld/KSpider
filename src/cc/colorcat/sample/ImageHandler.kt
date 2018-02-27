package cc.colorcat.sample

import cc.colorcat.kspider.Handler
import cc.colorcat.kspider.Scrap
import cc.colorcat.netbird4.Headers
import java.net.URI
import java.nio.file.Paths

/**
 * Created by cxx on 2018/2/2.
 * xx.ch@outlook.com
 */
class ImageHandler(private val saveDirectory: String) : Handler {
    private val imgReg = "^(http)(s)?://(.)*\\.(jpg|png|jpeg)$".toRegex()

    override fun handle(scrap: Scrap): Boolean {
        val targetUrl = scrap.data["url"]
        if (targetUrl != null && imgReg.matches(targetUrl)) {
            val folderName = scrap.data["dir"] ?: "Image"
            val fileName = targetUrl.substring(targetUrl.lastIndexOf('/') + 1)
            val savePath = Paths.get(saveDirectory, folderName, fileName).toFile()
            val headers = Headers.ofWithIgnoreNull(
                    listOf("Host", "Referer", UserAgent.NAME),
                    listOf(URI.create(targetUrl).host, scrap.uri.toString(), UserAgent.CHROME_MAC)
            )
            DownloadManager.download(targetUrl, savePath, headers)
            return true
        }
        return false
    }
}
