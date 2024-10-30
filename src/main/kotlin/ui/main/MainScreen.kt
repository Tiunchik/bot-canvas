package ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import board.BoardView
import board.hardcodeSelectedGraph
import common.rememberViewModel
import ui.LeftCanvas
import view.ApplicationState


@Composable
fun MainScreen(
    appState: ApplicationState = remember { ApplicationState() },
    boardView : BoardView = rememberViewModel { BoardView(hardcodeSelectedGraph) }
) {
//    // Переменная для отслеживания, было ли уже обработано событие Ctrl + Z
//    var commandExecuted by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()


//        .onPreviewKeyEvent { event ->
//            println("PRESS")
//            // Проверка нажатия Ctrl + Z
////                if (event.type == KeyEventType.KeyDown && event.isCtrlPressed && event.key == Key.Z) {
////                    println( "Ctrl + Z detected!")
////                    true // Указываем, что событие обработано
////                } else {
////                    println( "Ctrl + Z detected! NO!")
////                    false // Продолжаем обработку события
////                }
//
//            if (event.type == KeyEventType.KeyDown && event.isCtrlPressed && event.key == Key.Z) {
//                if (!commandExecuted) { // Обрабатываем только первое нажатие
//                    println(message = "Ctrl + Z detected!")
//                    commandExecuted = true
//                }
//                true
//            } else if (event.type == KeyEventType.KeyUp && event.key == Key.Z) {
//                // Сбрасываем флаг после отпускания клавиши, чтобы можно было снова обрабатывать комбинацию
//                commandExecuted = false
//                true
//            } else {
//                false
//            }
//        }


    ) {
        // Верхнее меню
        TopMenu(appState, boardView)

        Divider(
            modifier = Modifier
                .padding(58.dp),
            color = Color.Blue
        )

        // Нижняя часть экрана: Canvas слева и меню справа
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp) // Отступ для верхнего меню
        ) {
            // Левая часть - Canvas (80% ширины экрана)
            LeftCanvas(
                modifier = Modifier
                    .weight(0.80f) // 80% ширины экрана
                    .fillMaxHeight(),
                color = appState.boxColor,
                appState = appState,
                view = boardView
            )

            // Правая часть - меню с прокруткой (20% ширины экрана, минимум 100px)
            RightMenu(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(min = 300.dp) // Минимальная ширина 100 пикселей
                    .weight(0.20f),
                onMenuItemClick = {
                    appState.nodes = appState.nodes.plus(it).toMutableList()
                    boardView.addNode(it)
                }
            )
        }
    }
}
