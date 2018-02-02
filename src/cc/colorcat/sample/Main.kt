package cc.colorcat.sample

import cc.colorcat.kspider.EventListener
import cc.colorcat.kspider.KSpider
import cc.colorcat.kspider.Scrap
import cc.colorcat.kspider.Seed
import cc.colorcat.kspider.internal.Log

/**
 * Created by cxx on 2018/2/2.
 * xx.ch@outlook.com
 */
val spider = KSpider.Builder()
        .registerParser("bing", BingParser())
        .registerHandler("bing", BingHandler("E:\\Spider"))
        .registerParser("image", HDWallpaperParser())
        .registerParser("image", ZhuoKuParser())
        .registerHandler("image", ImageHandler("E:\\Spider"))
        .eventListener(object : EventListener {
            override fun onSuccess(seed: Seed) {
            }

            override fun onFailure(seed: Seed, reason: Throwable) {
            }

            override fun onReachedMaxDepth(seed: Seed) {
            }

            override fun onHandled(scrap: Scrap) {
                Log.d("KSpider", "handled, scrap = $scrap")
            }
        })
        .maxDepth(3)
        .build()


fun main(args: Array<String>) {
    val tagAndUris: Map<String, List<String>> = mapOf(
            "bing" to listOf("https://bing.ioliu.cn/"),
            "image" to listOf("http://www.zhuoku.com/zhuomianbizhi/jing-car/20180126214429(1).htm#turn", "https://www.hdwallpapers.in/")
    )
    spider.start(tagAndUris)
}
