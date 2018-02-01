package cc.colorcat.kspider

import java.net.URI
import java.util.*

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class Dispatcher {
    private lateinit var spider: KSpider
    private val reachedMaxDepth = LinkedList<Seed>()
    private val finished = LinkedList<Call>()
    private val waiting = LinkedList<Call>()
    private val running = LinkedList<Call>()

    @Synchronized
    internal fun setSpider(spider: KSpider) {
        this.spider = spider
    }

    @Synchronized
    internal fun enqueue(calls: List<Call>, depthFirst: Boolean) {
        val filters = calls.filter { !existsUri(it.seed.uri) }
        if (depthFirst) {
            waiting.addAll(0, filters)
        } else {
            waiting.addAll(filters)
        }
        promoteCalls()
    }

    private fun promoteCalls() {
        if (running.size >= spider.maxSeedOnRunning) return
        if (!waiting.isEmpty()) {
            val iterator = waiting.iterator()
            while (iterator.hasNext()) {
                val call = iterator.next()
                running.add(call)
                spider.executor.submit(call)
                iterator.remove()
                if (running.size >= spider.maxSeedOnRunning) break
            }
        } else if (running.isEmpty()) {
            onAllFinished()
        }
    }

    private fun onAllFinished() {
        val group = finished.groupBy { if (it.retryCount > spider.maxRetry) "failed" else "success" }
        val success = group["success"]?.map { it.seed } ?: emptyList()
        val failed = group["failed"]?.map { it.seed } ?: emptyList()
        spider.seedJar.save(success, failed, reachedMaxDepth)
    }

    @Synchronized
    internal fun finished(call: Call, reason: Throwable?) {
        val seed = call.seed
        running.removeIf { seed.uri == it.seed.uri }
        if (reason == null) {
            finished.add(call) // successful
            promoteCalls()
            spider.listener.onSuccess(seed)
        } else {
            call.incrementRetryCount()
            if (call.retryCount <= spider.maxRetry) {
                enqueue(listOf(call), spider.depthFirst) // retry
            } else {
                finished.add(call) // failed
                promoteCalls()
                spider.listener.onFailure(seed, reason)
            }
        }
    }

    internal fun handled(scrap: Scrap) {
        spider.listener.onHandled(scrap)
    }

    internal fun onReachedMaxDepth(seed: Seed) {
        synchronized(reachedMaxDepth) {
            reachedMaxDepth.add(seed)
        }
        spider.listener.onReachedMaxDepth(seed)
    }

    private fun existsUri(uri: URI) = running.any { uri == it.seed.uri } || waiting.any { uri == it.seed.uri } || finished.any { uri == it.seed.uri }
}
