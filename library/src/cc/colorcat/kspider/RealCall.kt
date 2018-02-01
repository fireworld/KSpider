package cc.colorcat.kspider

import cc.colorcat.kspider.internal.mutableListWith
import java.io.IOException

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class RealCall(override val seed: Seed, private val spider: KSpider) : Call {
    private var _retryCount = 0

    override val retryCount: Int
        @Synchronized
        get() = _retryCount

    @Synchronized
    override fun incrementRetryCount() {
        ++_retryCount
    }

    override fun execute() {
        var reason: Throwable? = null
        try {
            val newSeeds = getScrapsWithInterceptorChain()
            if (!newSeeds.isEmpty()) {
                spider.mapAndEnqueue(newSeeds)
            }
        } catch (e: Throwable) {
            reason = e
        } finally {
            spider.dispatcher.finished(this, reason)
        }
    }

    override fun run() {
        execute()
    }

    @Throws(IOException::class)
    private fun getScrapsWithInterceptorChain(): List<Scrap> {
        //todo
        val users = spider.interceptors
        val interceptors = mutableListWith<Interceptor>(users.size + 3)
        val chain = RealInterceptorChain(seed, spider.connection, spider.parser, interceptors, 0)
        return chain.proceed(seed)
    }
}
