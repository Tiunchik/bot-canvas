package common.compose

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

val Dispatchers.ViewModel: CoroutineDispatcher by lazy {
    Executors.newThreadPerTaskExecutor(
        ProxyNamedThreadFactory(Thread.ofVirtual().factory(), "VM Thread")
    ).asCoroutineDispatcher()
}

class ProxyNamedThreadFactory(
    private val factory: ThreadFactory,
    private val threadNamePrefix: String
) : ThreadFactory {
    private var counter = 0

    override fun newThread(r: Runnable): Thread =
        factory.newThread(r).also { it.name = "$threadNamePrefix-$counter" }
}

