package cc.colorcat.kspider

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class SeedsCleanerInterceptor(private val spider: KSpider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): List<Scrap> {
        val scraps = chain.proceed(chain.seed).toMutableList()
        val iterator = scraps.iterator()
        while (iterator.hasNext()) {
            val scrap = iterator.next()
            if (scrap.depth > spider.maxDepth) {
                iterator.remove()
                spider.dispatcher.onReachedMaxDepth(scrap)
            }
        }
        return scraps
    }
}
