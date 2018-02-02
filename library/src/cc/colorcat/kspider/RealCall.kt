package cc.colorcat.kspider

import cc.colorcat.kspider.internal.Log
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
            Log.e(e)
        } finally {
            spider.dispatcher.finished(this, reason)
        }
    }

    override fun run() {
        execute()
    }

    @Throws(IOException::class)
    private fun getScrapsWithInterceptorChain(): List<Scrap> {
        val users = spider.interceptors
        val interceptors = mutableListWith<Interceptor>(users.size + 3)
        interceptors.add(SeedsCleanerInterceptor(spider))
        interceptors.add(HandlerInterceptor(spider))
        interceptors.addAll(users)
        interceptors.add(ConnectionInterceptor())
        val chain = RealInterceptorChain(seed, spider.connection, spider.parser, interceptors, 0)
        return chain.proceed(seed)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RealCall

        if (seed != other.seed) return false

        return true
    }

    override fun hashCode(): Int {
        return seed.hashCode()
    }

    override fun toString(): String {
        return "RealCall(seed=$seed, retryCount=$_retryCount)"
    }
}
