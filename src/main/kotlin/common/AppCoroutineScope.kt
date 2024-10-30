package common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun Ctx.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = AppCoroutineScope.launch(context, start, block)

fun <T> Ctx.async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> = this.coroutineScope.async(context, start, block)


object AppCoroutineScope : CoroutineScope, AutoCloseable {
    /**
     * Без этого, проект не поднимается (падает на при запуске Любой Coroutine)
     *
     * ##### UI Error window
     * Module with the Main dispatcher is missing. Add dependency providing the Main dispatcher, e.g. 'kotlinx-coroutines-android' and ensure it has the same version as 'kotlinx-coroutines-core'
     *
     * ##### GPT
     * Сообщение об ошибке указывает на то, что в проекте отсутствует модуль с главным диспетчером
     * (Main dispatcher), который отвечает за выполнение корутин на главном потоке.
     * В Android эта проблема решается добавлением зависимости kotlinx-coroutines-android,
     * но для Kotlin Desktop нужно добавить поддержку диспетчера для других платформ.
     * - Подключите адаптер coroutine для Swing
     * - Подключите адаптер coroutine для JavaFX
     */
    private val mainCoroutineDispatcher =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val job = Job() // Родительский job для всех корутин
    override val coroutineContext = mainCoroutineDispatcher + job // Контекст корутин


    /**
     * Отмена всех корутин перед закрытием приложения
     */
    override fun close() {
        job.cancel()
    }
}