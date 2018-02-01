package cc.colorcat.kspider.internal

import cc.colorcat.kspider.EventListener
import cc.colorcat.kspider.Scrap
import cc.colorcat.kspider.Seed
import cc.colorcat.kspider.SeedJar
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal fun <T> List<T>.toImmutableList(): List<T> = Collections.unmodifiableList(ArrayList(this))

internal fun <T> MutableList<T>.addIfAbsent(t: T): Boolean {
    if (!this.contains(t)) {
        return this.add(t)
    }
    return false
}

internal inline fun <T> mutableListWith(initCapacity: Int): MutableList<T> = ArrayList(initCapacity)

internal fun <K, T> Map<K, List<T>>.toImmutableMap(): Map<K, List<T>> {
    val result = mutableMapOf<K, List<T>>()
    this.forEach { result[it.key] = it.value.toImmutableList() }
    return Collections.unmodifiableMap(result)
}

internal fun <K, T> Map<K, List<T>>.toMutableMap(): MutableMap<K, MutableList<T>> {
    val result = mutableMapOf<K, MutableList<T>>()
    this.forEach { result[it.key] = it.value.toMutableList() }
    return result
}

internal fun defaultService(): ExecutorService {
    val executor = ThreadPoolExecutor(
            8,
            10,
            60L,
            TimeUnit.SECONDS,
            LinkedBlockingDeque(),
            ThreadPoolExecutor.DiscardOldestPolicy()
    )
    executor.allowCoreThreadTimeOut(true)
    return executor
}

internal val emptyEventListener: EventListener by lazy {
    object : EventListener {
        override fun onSuccess(seed: Seed) {
        }

        override fun onFailure(seed: Seed, reason: Throwable) {
        }

        override fun onReachedMaxDepth(seed: Seed) {
        }

        override fun onHandled(scrap: Scrap) {
        }
    }
}

internal val emptySeedJar: SeedJar by lazy {
    object : SeedJar {
        override fun save(success: List<Seed>, failed: List<Seed>, reachedMaxDepth: List<Seed>) {}

        override fun load(): List<Seed> = emptyList()
    }
}
