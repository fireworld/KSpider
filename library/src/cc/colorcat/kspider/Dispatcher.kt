package cc.colorcat.kspider

import java.util.*

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class Dispatcher(private val spider: KSpider) {
    private val reachedMaxDepth = LinkedList<Seed>()
    private val finished = LinkedList<Call>()
    private val waiting = LinkedList<Call>()
    private val running = LinkedList<Call>()

    @Synchronized
    internal fun enqueue(calls: List<Call>, depthFirst: Boolean) {
        val filters = calls.filter { call ->
            val uri = call.seed.uri
            running.none { uri == it.seed.uri } && waiting.none { uri == it.seed.uri } && finished.none { uri == it.seed.uri }
        }
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
                if (running.size >= spider.maxSeedOnRunning) return
            }
        } else if (running.isEmpty()) {
            onAllFinished()
        }
    }

    private fun onAllFinished() {
        val s = "success"
        val f = "failed"
        val group = finished.groupBy { if (it.retryCount > spider.maxRetry) f else s }
        val success = group[s]?.map { it.seed } ?: emptyList()
        val failed = group[f]?.map { it.seed } ?: emptyList()
        spider.seedJar.save(success, failed, reachedMaxDepth)
    }

    @Synchronized
    internal fun finished(call: Call, reason: Throwable?) {
        val seed = call.seed
        running.removeIf { seed.uri == it.seed.uri }
        if (reason == null) {
            finished.add(call) // successful
            promoteCalls()
            spider.eventListener.onSuccess(seed)
        } else {
            call.incrementRetryCount()
            if (call.retryCount <= spider.maxRetry) {
                enqueue(listOf(call), spider.depthFirst) // retry
            } else {
                finished.add(call) // failed
                promoteCalls()
                spider.eventListener.onFailure(seed, reason)
            }
        }
    }

    internal fun handled(scrap: Scrap) {
        spider.eventListener.onHandled(scrap)
    }

    internal fun onReachedMaxDepth(seed: Seed) {
        synchronized(reachedMaxDepth) {
            reachedMaxDepth.add(seed)
        }
        spider.eventListener.onReachedMaxDepth(seed)
    }
}
