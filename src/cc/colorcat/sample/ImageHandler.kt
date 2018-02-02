package cc.colorcat.sample

import cc.colorcat.kspider.Handler
import cc.colorcat.kspider.Scrap
import cc.colorcat.netbird4.Headers
import java.io.File
import java.net.URI

/**
 * Created by cxx on 2018/2/2.
 * xx.ch@outlook.com
 */
class ImageHandler(private val saveDirectory: File) : Handler {
    private val imgReg = "^(http)(s)?://(.)*\\.(jpg|png|jpeg)$".toRegex()

    override fun handle(scrap: Scrap): Boolean {
        val targetUrl = scrap.data["url"]
        if (targetUrl != null && imgReg.matches(targetUrl)) {
            val folderName = scrap.data["dir"]
            val fileName = targetUrl.substring(targetUrl.lastIndexOf('/') + 1)
            val savePath = createSavePath(folderName, fileName)
            val headers = Headers.ofWithIgnoreNull(listOf("Host", "Referer"), listOf(URI.create(targetUrl).host, scrap.uri.toString()))
            DownloadManager.download(targetUrl, savePath, headers)
            return true
        }
        return false
    }

    private fun createSavePath(folderName: String?, fileName: String): File {
        val saveDir = if (folderName != null) File(saveDirectory, folderName) else saveDirectory
        saveDir.exists() || saveDir.mkdirs()
        return File(saveDir, fileName)
    }
}
