package ui

import LINE_LEVEL
import NODE_LEVEL
import SURFACE_LEVEL
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import view.ApplicationState
import kotlin.math.max
import kotlin.math.min

private val windowSize = 2000.dp
private val mapSize = 6000.dp

// Компонент для рисования на Canvas
@Composable
fun LeftCanvas(modifier: Modifier = Modifier, color: Color, appState: ApplicationState) {
    // Отслеживание положения курсора мыши в реалтайме ^_^
    var cursorPoint by remember { mutableStateOf(Offset.Zero) }
    // Состояние для хранения смещения карты
    var mapOffset by remember { mutableStateOf(Offset.Zero) }
    // Состояние для масштаба
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Основное окно
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
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
            .pointerInput(3) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        //  управление скроллом
                        event.changes.first().scrollDelta.y
                            .let {
                                if (it != 0f) {
                                    val zoomFactor = if (it > 0) 0.9f else 1.1f
                                    scale = max(0.5f, min(3f, scale * zoomFactor)) // Ограничиваем масштаб от 0.5x до 3x
                                }
                            }
                    }

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
                        if (event.buttons.isPrimaryPressed && appState.tempArrow.isDraw) {
                            appState.tempArrow.isDraw = false
                        }
                    }
                }
            }
        ) {
            DrawGrid(60, 6000f, 6000f)

            appState.apply {
                links.forEach {
                    Arrow(
                        modifier = Modifier
                            .zIndex(LINE_LEVEL)
                            .scale(scale),
                        startPoint = it.startNode.center,
                        endPoint = it.endNode.center
                    )
                }

                //Отрисовка узлов
                nodes.forEach {
                    DraggableNode(
                        modifier = Modifier
                            .zIndex(NODE_LEVEL)
                            .scale(scale),
                        appState = appState,
                        node = it,
                        color = color
                    )
                }

                // Протягивание линии от узла к узлу
                drawTempArrow(cursorPoint)
            }
        }
    }

}

@Composable
private fun ApplicationState.drawTempArrow(cursorPoint: Offset) {
    if (tempArrow.isDraw) Arrow(
        modifier = Modifier.zIndex(NODE_LEVEL + 1f),
        startPoint = tempArrow.startPoint,
        endPoint = cursorPoint
    )
}