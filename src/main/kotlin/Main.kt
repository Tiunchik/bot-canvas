import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import common.Ctx
import common.init
import kotlinx.coroutines.runBlocking
import ui.main.MainScreen

fun main() = application {
    Window(onCloseRequest = {
        runBlocking { Ctx.graphDataSource.saveStateToFile() } // Сохранить нынешний граф
        Ctx.coroutineScope.close() // отмена всех Короутин уровня приложение
        exitApplication() // выход из программы
    }) {
        MaterialTheme {
            Ctx.init()
            Ctx.graphDataSource.loadStateFromFile()

            MainScreen()
        }
    }
}

