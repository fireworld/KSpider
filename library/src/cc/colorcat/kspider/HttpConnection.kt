package cc.colorcat.kspider

import cc.colorcat.kspider.internal.close
import cc.colorcat.kspider.internal.isHttpUrl
import cc.colorcat.kspider.internal.parseCharset
import cc.colorcat.kspider.internal.readByteArray
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
open class HttpConnection() : Connection {
    private var snapshot: WebSnapshot? = null

    override fun get(uri: URI): WebSnapshot? {
        if (!isHttpUrl(uri)) throw UnsupportedOperationException("Unsupported uri, uri = $uri")
        if (snapshot != null && snapshot?.uri == uri) {
            return snapshot
        }
        snapshot = HttpConnection.Companion.doGet(uri)
        return snapshot
    }


    override fun clone(): Connection = HttpConnection()

    protected companion object {
        @Throws(IOException::class)
        fun doGet(uri: URI): WebSnapshot? {
            val conn = createConnection(uri, "GET")
            var input: InputStream? = null
            try {
                if (HttpURLConnection.HTTP_OK == conn.responseCode) {
                    input = conn.inputStream
                    if (input != null) {
                        val charset = parseCharset(conn.contentType)
                        val content = input.readByteArray()
                        return WebSnapshot.newWebSnapshot(uri, content, charset)
                    }
                }
            } finally {
                conn.disconnect()
                close(input)
            }
            return null
        }

        @Throws(IOException::class)
        private fun createConnection(uri: URI, method: String): HttpURLConnection {
            val conn = uri.toURL().openConnection() as HttpURLConnection
            conn.requestMethod = method
            conn.connectTimeout = 10000
            conn.readTimeout = 10000
            return conn
        }
    }
}
