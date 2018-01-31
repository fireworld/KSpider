package cc.colorcat.kspider

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
interface Call : Runnable {
    val retryCount: Int

    fun incrementRetryCount()

    val seed: Seed

    fun execute()

    interface Factory {
        fun newCall(seed: Seed): Call
    }
}
