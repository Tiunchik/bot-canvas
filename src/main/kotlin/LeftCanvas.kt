import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dto.CurrentLink
import dto.Node
import dto.ViewModel
import java.util.UUID

// Компонент для рисования на Canvas
@Composable
fun LeftCanvas(modifier: Modifier = Modifier, color: Color, model: ViewModel) {
    var cursorPosition by remember { mutableStateOf(Offset.Zero) }

    val windowSize = 2000.dp
    val mapSize = 6000.dp

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
                        cursorPosition = event.changes.first().position // Получаем позицию курсора
                        // Если мы создаём линию-связь и нажимаем левой клавишей мыши - прекратить
                        if (event.buttons.isPrimaryPressed && model.isCreateLine.value) {
                            model.isCreateLine.value = false
                        }
                    }
                }
            }
        ) {
            val linesMap = model.nodes.value.associateBy { it.id }
            //Отрисовываем связи
            model.links.value.forEach {
                val start = linesMap[it.startNode]
                val end = linesMap[it.endNode]
                if (start != null && end != null) {
                    NodeLine(start, end)
                }
            }

            //Отрисовываем узлы
            model.nodes.value.forEach {
                DraggableNode(model, it, color)
            }

            //Протягивание линии от узла к узлу
            if (model.isCreateLine.value) {
                Line(model.startPosition.value, cursorPosition)
            }
        }

    }


}