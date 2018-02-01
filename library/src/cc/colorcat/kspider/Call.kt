package cc.colorcat.kspider

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
interface Call : Runnable {
    val seed: Seed

    val retryCount: Int

    fun incrementRetryCount()

    fun execute()

    interface Factory {
        fun newCall(seed: Seed): Call
    }
}
