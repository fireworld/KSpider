package cc.colorcat.kspider

import java.net.URI
import java.nio.charset.Charset
import java.util.*

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
class WebSnapshot private constructor(
        val uri: URI,
        val content: ByteArray,
        private val charset: Charset?
) {
    companion object {
        @JvmStatic
        fun newWebSnapshot(uri: URI, content: ByteArray, charset: Charset?) = WebSnapshot(uri, content, charset)
    }

    fun contentToString(): String = if (charset != null) String(content, charset) else String(content)

    fun contentToString(charset: String): String = String(content, this.charset ?: Charset.forName(charset))

    fun contentToString(charset: Charset): String = String(content, this.charset ?: charset)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebSnapshot

        if (uri != other.uri) return false
        if (charset != other.charset) return false
        if (!Arrays.equals(content, other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + (charset?.hashCode() ?: 0)
        result = 31 * result + (content?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }

    override fun toString(): String {
        return "WebSnapshot(uri=$uri, charset=$charset, content=${Arrays.toString(content)})"
    }
}
