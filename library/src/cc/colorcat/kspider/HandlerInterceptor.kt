package cc.colorcat.kspider

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class HandlerInterceptor(private val spider: KSpider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): List<Scrap> {
        val scraps = chain.proceed(chain.seed).toMutableList()
        scraps.removeIf { scrap ->
            if (scrap.data.isEmpty()) {
                false
            } else {
                var handled = false
                spider.handlers[scrap.tag]?.forEach { if (it.handle(scrap)) handled = true }
                if (handled) {
                    spider.dispatcher.handled(scrap)
                }
                handled
            }
        }

//        val iterator = scraps.toMutableList().iterator()
//        while (iterator.hasNext()) {
//            val scrap = iterator.next()
//            if (scrap.data.isEmpty()) continue
//            var handled = false
//            spider.handlers[scrap.tag]?.forEach { if (it.handle(scrap)) handled = true }
//            if (handled) {
//                iterator.remove()
//                spider.dispatcher.handled(scrap)
//            }
//        }
        return scraps
    }
}
