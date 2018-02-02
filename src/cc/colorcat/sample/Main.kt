package cc.colorcat.sample

import cc.colorcat.kspider.EventListener
import cc.colorcat.kspider.KSpider
import cc.colorcat.kspider.Scrap
import cc.colorcat.kspider.Seed
import cc.colorcat.kspider.internal.Log
import java.io.File

/**
 * Created by cxx on 2018/2/2.
 * xx.ch@outlook.com
 */
val spider = KSpider.Builder()
        .registerParser("bing", BingParser())
        .registerHandler("bing", BingHandler())
        .registerParser("image", HdParser())
        .registerHandler("image", ImageHandler(File("E:\\Spider")))
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
        .maxDepth(10)
        .build()


fun main(args: Array<String>) {
    spider.start("image", "https://www.hdwallpapers.in/")
}
