package cc.colorcat.kspider

import cc.colorcat.kspider.internal.close
import cc.colorcat.kspider.internal.isHttpUrl
import cc.colorcat.kspider.internal.parseCharset
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI
import java.nio.charset.Charset

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
class HttpConnection(private val charset: Charset) : Connection {
    private lateinit var snapshot: WebSnapshot

    override fun get(uri: URI): WebSnapshot {
        if (!isHttpUrl(uri)) throw UnsupportedOperationException("Unsupported uri, uri = $uri")
        if (this::snapshot.isInitialized && snapshot.resource != null && snapshot.uri == uri) {
            return snapshot
        }
        snapshot = onGet(uri)
        return snapshot
    }


    override fun clone(): Connection = HttpConnection(charset)

    @Throws(IOException::class)
    private fun onGet(uri: URI): WebSnapshot {
        var conn: HttpURLConnection? = null
        var input: InputStream? = null
        try {
            conn = uri.toURL().openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 10000
            conn.readTimeout = 10000
            if (HttpURLConnection.HTTP_OK == conn.responseCode) {
                val chr = parseCharset(conn.contentType, charset)
                input = conn.inputStream
                if (input != null) {
                    val content = input.bufferedReader(chr).readText()
                    return WebSnapshot.newWebSnapshot(uri, chr, content)
                }
            }
        } finally {
            conn?.disconnect()
            close(input)
        }
        return WebSnapshot.newWebSnapshot(uri, charset)
    }
}