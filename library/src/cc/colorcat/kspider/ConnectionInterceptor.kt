package cc.colorcat.kspider

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
internal class ConnectionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): List<Scrap> {
        val seed = chain.seed
        val snapshot = chain.connection.get(seed.uri)
        return chain.parser.parse(seed, snapshot)
    }
}
