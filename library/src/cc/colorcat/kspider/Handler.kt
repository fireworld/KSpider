package cc.colorcat.kspider

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
interface Handler {
    fun handle(scrap: Scrap): Boolean
}
