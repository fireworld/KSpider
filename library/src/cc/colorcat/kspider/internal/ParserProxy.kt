package cc.colorcat.kspider.internal

import cc.colorcat.kspider.Parser
import cc.colorcat.kspider.Scrap
import cc.colorcat.kspider.Seed
import cc.colorcat.kspider.WebSnapshot

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class ParserProxy(private val parsers: List<Parser>) : Parser {

    override fun parse(seed: Seed, snapshot: WebSnapshot): List<Scrap> {
        val scraps: MutableList<Scrap> = mutableListOf()
        if (snapshot.resource != null) {
            parsers.forEach { scraps.addAll(it.parse(seed, snapshot)) }
        }
        return scraps
    }
}
