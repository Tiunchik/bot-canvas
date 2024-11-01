package common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.*

/**
 * todo - обрати внимание! когда будет переключение графа для отрисовки (set(newGraph)),
 *      то все короутины от старого графа долны Отмениться ViewModel.clear()
 */
@Composable fun <VM : ViewModel> rememberViewModel(viewModelInit: () -> VM): VM =
    remember(viewModelInit).also { DisposableEffect(Unit) { onDispose { it.cancelAllCoroutines() } } }

abstract class ViewModel {
    protected val coroutineScope: CoroutineScope by lazy {
        CoroutineScope(
            Dispatchers.ViewModel
                    + SupervisorJob()
                    + CoroutineName(
                "VM Scope of ${this.javaClass.canonicalName}"
            )
        )
    }

    fun cancelAllCoroutines() = coroutineScope.cancel() // Отмена всех корутин при необходимости

    /**
     * Специально для назначения Действия на кнопки
     * ```
     * Button(onClick = viewModel.executeAsync {
     *      // что-то делает, в том числе suspend calls
     * })
     * ```
     *
     * Запускает Задачу от viewModel.coroutineScope.
     * Когда viewModel выйдет из композици, все Корутины будут отменены, в том числе, запущенные таким образом
     */
    fun executeAsync(block: suspend CoroutineScope.() -> Unit): () -> Unit =
        { this.coroutineScope.launch(block = block) }
}
