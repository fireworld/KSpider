package cc.colorcat.sample

import cc.colorcat.kspider.internal.Log
import cc.colorcat.netbird4.*
import java.io.File

/**
 * Created by cxx on 2018/2/2.
 * xx.ch@outlook.com
 */
object DownloadManager {
    private val netBird by lazy {
        NetBird.Builder("https://www.google.com.hk")
                .connectTimeOut(30000)
                .readTimeOut(30000)
                .maxRunning(10)
                .logLevel(Level.NOTHING)
                .build()
    }
    var maxRetry: Int = 3
        set(value) {
            if (value > 0) {
                field = value
            }
        }
    private val tasks = mutableMapOf<String, Int>()
    private val emptyHeaders = Headers.ofWithIgnoreNull(emptyMap())

    fun download(url: String, savePath: File, headers: Headers = emptyHeaders) {
        if (!tasks.containsKey(url)) {
            tasks[url] = 0
            realDownload(url, savePath, headers)
        }
    }

    private fun realDownload(url: String, savePath: File, headers: Headers) {
        val request = MRequest.Builder<File>(FileParser.create(savePath))
                .url(url)
                .addHeaders(headers)
                .listener(object : MRequest.SimpleListener<File>() {
                    override fun onSuccess(result: File) {
                        Log.i("Download", "success, save path=$result")
                    }

                    override fun onFailure(code: Int, msg: String) {
                        val count = tasks[url] as Int
                        if (count < maxRetry) {
                            tasks[url] = count + 1
                            realDownload(url, savePath, headers)
                        } else {
                            Log.e("Download", "download failed, code=$code, msg=$msg, url=$url")
                        }
                    }
                })
                .build()
        netBird.send(request)
    }
}
