package cc.colorcat.kspider

import java.io.IOException
import java.net.URI

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
interface Connection : Cloneable {
    @Throws(IOException::class)
    fun get(uri: URI): WebSnapshot

    public override fun clone(): Connection
}
