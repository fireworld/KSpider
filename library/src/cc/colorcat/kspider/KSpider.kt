package cc.colorcat.kspider

import cc.colorcat.kspider.internal.*
import java.util.concurrent.ExecutorService

/**
 * Created by cxx on 2018/2/1.
 * xx.ch@outlook.com
 */
class KSpider internal constructor(builder: Builder) : Call.Factory {
    val handlers: Map<String, List<Handler>> = builder.handlers.toImmutableMap()
    val interceptors: List<Interceptor> = builder.interceptors.toImmutableList()
    val parsers: List<Parser> = builder.parsers.toImmutableList()
    internal val parser: Parser = ParserProxy(parsers)
    private val _connection = builder.connection
    val connection: Connection
        get() = _connection.clone()
    val executor: ExecutorService = builder.executor
    internal val dispatcher: Dispatcher = builder.dispatcher
    val depthFirst: Boolean = builder.depthFirst
    val maxRetry: Int = builder.maxRetry
    val maxSeedOnRunning: Int = builder.maxSeedOnRunning
    val maxDepth: Int = builder.maxDepth
    val listener: EventListener = builder.listener
    val seedJar: SeedJar = builder.seedJar

    init {
        dispatcher.setSpider(this)
    }

    override fun newCall(seed: Seed): Call {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    internal fun mapAndEnqueue(seeds: List<Seed>) {

    }

    fun newBuilder(): Builder = Builder(this)


    class Builder {
        internal val handlers: MutableMap<String, MutableList<Handler>>
        internal val interceptors: MutableList<Interceptor>
        internal val parsers: MutableList<Parser>
        internal var connection: Connection
            private set
        internal var executor: ExecutorService
            private set
        internal var dispatcher: Dispatcher
            private set
        internal var depthFirst: Boolean
            private set
        internal var maxRetry: Int
            private set
        internal var maxSeedOnRunning: Int
            private set
        internal var maxDepth: Int
            private set
        internal var listener: EventListener
            private set
        internal var seedJar: SeedJar
            private set

        constructor() {
            handlers = mutableMapOf()
            interceptors = mutableListOf()
            parsers = mutableListOf()
            connection = HttpConnection()
            executor = defaultService()
            dispatcher = Dispatcher()
            depthFirst = false
            maxRetry = 3
            maxSeedOnRunning = 20
            maxDepth = 100
            listener = emptyEventListener
            seedJar = emptySeedJar
        }

        internal constructor(spider: KSpider) {
            handlers = spider.handlers.toMutableMap()
            interceptors = spider.interceptors.toMutableList()
            parsers = spider.parsers.toMutableList()
            connection = spider._connection
            executor = spider.executor
            dispatcher = spider.dispatcher
            depthFirst = spider.depthFirst
            maxRetry = spider.maxRetry
            maxSeedOnRunning = spider.maxSeedOnRunning
            maxDepth = spider.maxDepth
            listener = spider.listener
            seedJar = spider.seedJar
        }

        fun registerHandler(tag: String, handler: Handler): Builder {
            val hs = this.handlers.computeIfAbsent(tag) { mutableListOf() }
            hs.addIfAbsent(handler)
            return this
        }

        fun removeHandler(tag: String, handler: Handler): Builder {
            this.handlers[tag]?.remove(handler)
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            this.interceptors.addIfAbsent(interceptor)
            return this
        }

        fun removeInterceptor(interceptor: Interceptor): Builder {
            this.interceptors.remove(interceptor)
            return this
        }

        fun addParser(parser: Parser): Builder {
            this.parsers.addIfAbsent(parser)
            return this
        }

        fun removeParser(parser: Parser): Builder {
            this.parsers.remove(parser)
            return this
        }

        fun connection(connection: Connection): Builder {
            this.connection = connection
            return this
        }

        fun executor(executor: ExecutorService): Builder {
            this.executor = executor
            return this
        }

        fun depthFirst(depthFirst: Boolean): Builder {
            this.depthFirst = depthFirst
            return this
        }

        fun maxRetry(maxRetry: Int): Builder {
            if (maxRetry < 0) throw IllegalArgumentException("maxRetry($maxRetry) < 0")
            this.maxRetry = maxRetry
            return this
        }

        fun maxSeedOnRunning(maxSeedOnRunning: Int): Builder {
            if (maxSeedOnRunning < 1) throw IllegalArgumentException("maxSeedOnRunning($maxSeedOnRunning) < 1")
            this.maxSeedOnRunning = maxSeedOnRunning
            return this
        }

        fun maxDepth(maxDepth: Int): Builder {
            if (maxDepth < 1) throw IllegalArgumentException("maxDepth($maxDepth) < 1")
            this.maxDepth = maxDepth
            return this
        }

        fun eventListener(listener: EventListener): Builder {
            this.listener = listener
            return this
        }

        fun seedJar(seedJar: SeedJar): Builder {
            this.seedJar = seedJar
            return this
        }

        fun build(): KSpider = KSpider(this)
    }
}
