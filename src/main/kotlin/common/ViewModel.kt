package common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.*

/**
 * todo - обрати внимание! когда будет переключение графа для отрисовки (set(newGraph)),
 *      то все короутины от старого графа долны Отмениться ViewModel.clear()
 */
@Composable
fun <VM : ViewModel> rememberViewModel(viewModelInit: () -> VM): VM {
    val vm = remember(viewModelInit)
    // Используем DisposableEffect для вызова clear() при уничтожении Composable
    DisposableEffect(Unit) {
        onDispose {
            vm.clear() // Отмена всех корутин при уничтожении Composable
        }
    }
    return vm
}

abstract class ViewModel {
    val viewModelScope: CoroutineScope by lazy {
        CoroutineScope(
            Dispatchers.ViewModel + SupervisorJob()
                    + CoroutineName(
                "VM Scope of ${this.javaClass.canonicalName}"
            )
        )
    }

    fun clear() = viewModelScope.cancel() // Отмена всех корутин при необходимости
}