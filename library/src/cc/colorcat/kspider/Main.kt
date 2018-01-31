package cc.colorcat.kspider

import java.net.URI

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
fun main(args: Array<String>) {
    val seed = Seed("img", URI.create("http://www.baidu.com"), 0, emptyMap())
    val scrap = Scrap("img", URI.create("http://www.baidu.com"), 0, emptyMap())
    println(seed)
    println(scrap)
    println(seed == scrap)
}
