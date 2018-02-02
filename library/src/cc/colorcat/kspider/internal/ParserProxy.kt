package cc.colorcat.kspider.internal

import cc.colorcat.kspider.Parser
import cc.colorcat.kspider.Scrap
import cc.colorcat.kspider.Seed
import cc.colorcat.kspider.WebSnapshot
import java.util.*

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class ParserProxy(private val parsers: Map<String, List<Parser>>) : Parser {

    override fun parse(seed: Seed, snapshot: WebSnapshot): List<Scrap> {
        val parsers = this.parsers[seed.tag]
        if (parsers == null || parsers.isEmpty()) return emptyList()
        val scraps = LinkedList<Scrap>()
        parsers.forEach { scraps.addAll(it.parse(seed, snapshot)) }
        return scraps
    }
}
