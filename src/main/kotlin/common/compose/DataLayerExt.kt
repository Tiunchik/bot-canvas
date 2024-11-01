package common.compose

import kotlinx.coroutines.*

/**
 * @param name имя таски/coroutine для debug & log
 */
fun launchIO(name: String, action: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(CoroutineName(name) + Dispatchers.IO).launch(block = action)
