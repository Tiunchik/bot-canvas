package ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import board.BoardView
import board.hardcodeSelectedGraphUUID
import common.rememberViewModel
import ui.LeftCanvas
import view.ApplicationState


@Composable
fun MainScreen(
    appState: ApplicationState = remember { ApplicationState() },
    boardView: BoardView = rememberViewModel { BoardView(hardcodeSelectedGraphUUID) }
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
                    boardView.addNode(it)
                }
            )
        }
    }
}
