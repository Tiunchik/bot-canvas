package common

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

private val vmDispatcher =
    Executors.newThreadPerTaskExecutor(
        ProxyNamedThreadFactory(Thread.ofVirtual().factory(), "VM Thread")
    )
        .asCoroutineDispatcher()
val Dispatchers.ViewModel: CoroutineDispatcher get() = vmDispatcher

class ProxyNamedThreadFactory(
    private val factory: ThreadFactory,
    private val threadNamePrefix: String
) : ThreadFactory {
    private var counter = 0

    override fun newThread(r: Runnable): Thread =
        factory.newThread(r).also { it.name = "$threadNamePrefix-$counter" }
}

/**
 * @param name имя таски/coroutine для debug & log
 */
fun launchIO(name: String, action: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(CoroutineName(name) + Dispatchers.IO).launch(block = action)