package cc.colorcat.kspider

import java.net.URI
import java.nio.charset.Charset

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
class WebSnapshot private constructor(val uri: URI, private val charset: Charset, val resource: String?) {
    companion object {
        fun newWebSnapshot(uri: URI, charset: Charset, resource: String? = null) = WebSnapshot(uri, charset, resource)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebSnapshot

        if (uri != other.uri) return false
        if (charset != other.charset) return false
        if (resource != other.resource) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + charset.hashCode()
        result = 31 * result + (resource?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "WebSnapshot(uri=$uri, charset=$charset, resource=$resource)"
    }
}
