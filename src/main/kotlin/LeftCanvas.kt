import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dto.ApplicationState

val windowSize = 2000.dp
val mapSize = 6000.dp

// Компонент для рисования на Canvas
@Composable
fun LeftCanvas(modifier: Modifier = Modifier, color: Color, appState: ApplicationState) {
    var cursorPoint by remember { mutableStateOf(Offset.Zero) }

    // Состояние для хранения смещения карты
    var mapOffset by remember { mutableStateOf(Offset.Zero) }

    // Основное окно
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
            .background(Color.Gray)
            // Жест перетаскивания карты
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val newOffset = mapOffset + dragAmount

                    // Ограничиваем смещение карты, чтобы не выйти за границы
                    mapOffset = Offset(
                        x = newOffset.x.coerceIn(-(mapSize.toPx() - windowSize.toPx()), 0f),
                        y = newOffset.y.coerceIn(-(mapSize.toPx() - windowSize.toPx()), 0f)
                    )
                }
            }
    ) {
        // Большая карта
        Box(modifier
            .offset { IntOffset(mapOffset.x.toInt(), mapOffset.y.toInt()) } // Смещаем карту
            .size(mapSize)
            .zIndex(SURFACE_LEVEL)
            .pointerInput(1) {
                awaitPointerEventScope {
                    //логика рисования линии между нодами после нажатие на создание линии в контексном меню
                    while (true) {
                        val event = awaitPointerEvent()
                        cursorPoint = event.changes.first().position // Получаем позицию курсора
                        // Если мы создаём стрелку и нажимаем ЛКМ - прекратить
//                        if (event.buttons.isPrimaryPressed && appState.isCreateLine) {
//                            model.isCreateLine = false
//                        }
                        if (event.buttons.isPrimaryPressed && appState.tempArrow.isDraw) {
//                            model.isCreateLine = false
                            appState.tempArrow.isDraw = false
                        }
                    }
                }
            }
        ) {
            val linesMap = appState.nodes.associateBy { it.id }
            //Отрисовываем связи
            appState.links.forEach {
                val start = linesMap[it.startNode]
                val end = linesMap[it.endNode]
                if (start != null && end != null) {
                    NodeLine(start, end)
                }
            }

            //Отрисовываем узлы
            appState.nodes.forEach {
                DraggableNode(appState, it, color)
            }

            //Протягивание линии от узла к узлу
//            if (appState.isCreateLine) {
//                Line(appState.startPosition, cursorPosition)
//            }
            appState.drawTempArrow(cursorPoint)
        }

    }


}

@Composable
private fun ApplicationState.drawTempArrow(cursorPos: Offset) {
    if (tempArrow.isDraw) Arrow(
        tempArrow.startPoint ?: throw RuntimeException("что-то не так..."),
        cursorPos
    )
}