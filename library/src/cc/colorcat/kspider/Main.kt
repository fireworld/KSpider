package cc.colorcat.kspider

import cc.colorcat.kspider.internal.mutableListWith

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
fun main(args: Array<String>) {
    val ints = mutableListWith<Int>(100)
    ints += 1..100
    println(ints)
    println("---------------------------------")
    val group = ints.groupBy { if (it > 50) "greater" else "less" }
    println(group["greater"])
    println(group["less"])
}
