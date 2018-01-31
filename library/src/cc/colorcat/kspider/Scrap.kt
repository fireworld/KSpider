package cc.colorcat.kspider

import java.net.URI

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
class Scrap internal constructor(tag: String, uri: URI, depth: Int, data: Map<String, String>) : Seed(tag, uri, depth, data) {
    override fun fill(data: Map<String, String>): Scrap {
        super.fill(data)
        return this
    }

    override fun fill(key: String, value: String): Scrap {
        super.fill(key, value)
        return this
    }

    override fun fillIfAbsent(data: Map<String, String>): Scrap {
        super.fillIfAbsent(data)
        return this
    }

    override fun fillIfAbsent(key: String, value: String): Scrap {
        super.fillIfAbsent(key, value)
        return this
    }
}
