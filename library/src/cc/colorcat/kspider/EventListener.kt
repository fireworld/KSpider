package cc.colorcat.kspider

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
interface EventListener {
    fun onSuccess(seed: Seed)

    fun onFailure(seed: Seed, reason: Throwable)

    fun onReachedMaxDepth(seed: Seed)

    fun onHandled(scrap: Scrap)
}
