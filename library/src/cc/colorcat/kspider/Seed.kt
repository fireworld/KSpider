package cc.colorcat.kspider

import com.sun.deploy.util.URLUtil
import java.net.MalformedURLException
import java.net.URI

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
open class Seed internal constructor(
        val tag: String,
        val uri: URI,
        val depth: Int,
        data: Map<String, String>
) {
    private val _data: MutableMap<String, String> = HashMap(data)

    val data
        get() = _data.toMap()

    fun newUriWithJoin(uri: String): String = this.uri.resolve(uri).toString()

    fun baseUrl(): String = try {
        URLUtil.getBase(this.uri.toURL()).toString()
    } catch (e: MalformedURLException) {
        this.uri.toString()
    }

    open fun fill(data: Map<String, String>): Seed {
        _data.putAll(data)
        return this
    }

    open fun fill(key: String, value: String): Seed {
        _data[key] = value
        return this
    }

    open fun fillIfAbsent(data: Map<String, String>): Seed {
        for ((key, value) in data) {
            _data.putIfAbsent(key, value)
        }
        return this
    }

    open fun fillIfAbsent(key: String, value: String): Seed {
        _data.putIfAbsent(key, value)
        return this
    }

    fun newSeedWithResetDepth(): Seed = Seed(tag, uri, 0, _data)

    fun newScrapWithFill(key: String, value: String): Scrap = Scrap(tag, uri, depth + 1, _data).fill(key, value)

    fun newScrapWithFill(data: Map<String, String>): Scrap = Scrap(tag, uri, depth + 1, _data).fill(data)

    fun newScrap(uri: String): Scrap = newScrap(URI.create(uri))

    fun newScrap(uri: URI): Scrap = Scrap(tag, uri, depth + 1, _data)

    fun newScrapWithJoin(uri: String): Scrap = newScrapWithJoin(URI.create(uri))

    fun newScrapWithJoin(uri: URI): Scrap = Scrap(tag, this.uri.resolve(uri), depth + 1, _data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Seed

        if (tag != other.tag) return false
        if (uri != other.uri) return false
        if (depth != other.depth) return false
        if (_data != other._data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + depth
        result = 31 * result + _data.hashCode()
        return result
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(tag='$tag', uri=$uri, depth=$depth, data=$_data)"
    }
}
