package cc.colorcat.kspider

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class SeedsCleanerInterceptor(private val spider: KSpider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): List<Scrap> {
        val scraps = chain.proceed(chain.seed).let { it as? MutableList ?: it.toMutableList() }
        scraps.removeIf {
            if (it.depth >= spider.maxDepth) {
                spider.dispatcher.onReachedMaxDepth(it)
                true
            } else {
                false
            }
        }
        return scraps
    }
}
