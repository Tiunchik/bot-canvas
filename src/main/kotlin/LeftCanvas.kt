import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.IntSize
import dto.CurrentLink
import dto.Node
import dto.ViewModel
import java.util.UUID

// Компонент для рисования на Canvas
@Composable
fun LeftCanvas(modifier: Modifier = Modifier, color: Color, model: ViewModel) {
    var currentLine by remember { mutableStateOf(CurrentLink(Offset.Zero, Offset.Zero, UUID.randomUUID())) }
    var cursorPosition by remember { mutableStateOf(Offset.Zero) }
    var startPosition by remember { mutableStateOf(Offset.Zero) }
    var startPositionIsRemembered by remember { mutableStateOf(false) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var boxOffset by remember { mutableStateOf(Offset.Zero) }
    var isCursorInsideBox by remember { mutableStateOf(false) }

    Box(modifier
        .fillMaxSize()
        .pointerInput(1) {

        }
    ) {
        model.nodes.value.forEach {
            DraggableNode(model,it, color)
        }
        model.links.value.forEach {

        }
        /**
         * Протягивание линии от кубика к кубику
         */
        if (model.isCreateLine.value) {
            Canvas(modifier = Modifier
                .fillMaxSize()
//                .onGloballyPositioned { coordinates ->
//                    // Получаем размер и положение Box на экране
//                    boxSize = coordinates.size
//                    boxOffset = Offset(coordinates.positionInWindow().x, coordinates.positionInWindow().y)
//                }
                .pointerInput(1) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            cursorPosition = event.changes.first().position // Получаем позицию курсора
                            if (event.buttons.isPrimaryPressed && startPositionIsRemembered) {
                                model.isCreateLine.value = false
                                startPositionIsRemembered = false
                            }
                            if (!startPositionIsRemembered) {
                                startPosition = event.changes.first().position
                                startPositionIsRemembered = true
//                                isCursorInsideBox = cursorPosition.isInside(boxOffset, boxSize)
                            }
                        }
                    }
                }
            ) {

                drawLine(color = Color.Black, startPosition, cursorPosition)
                // Здесь можно рисовать что-то с использованием позиции курсора, если нужно
            }
        }

    }
}