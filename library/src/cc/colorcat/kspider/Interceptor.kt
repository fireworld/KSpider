package cc.colorcat.kspider

import java.io.IOException

/**
 * Created by cxx on 18-1-31.
 * xx.ch@outlook.com
 */
interface Interceptor {
    @Throws(IOException::class)
    fun intercept(chain: Chain): List<Scrap>

    interface Chain {
        fun connection(): Connection

        fun parser(): Parser

        fun seed(): Seed

        @Throws(IOException::class)
        fun proceed(seed: Seed): List<Scrap>
    }
}
