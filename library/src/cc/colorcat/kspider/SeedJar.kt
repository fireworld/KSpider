package cc.colorcat.kspider

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
interface SeedJar {
    fun save(success: List<Seed>, failed: List<Seed>, reachedMaxDepth: List<Seed>)

    fun load(): List<Seed>
}
