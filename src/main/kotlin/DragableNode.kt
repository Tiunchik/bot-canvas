import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import dto.Link
import dto.Node
import dto.ViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableNode(model: ViewModel, node: Node, color: Color) {
    val nodeId = node.id
    var offset by remember { mutableStateOf(node.offset) }

    // Переменная для управления отображением меню
    var showMenu by remember { mutableStateOf(false) }
    // Переменная для хранения позиции клика
    var menuOffset by remember { mutableStateOf(IntOffset.Zero) }

    Box(modifier = Modifier
        .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
        .pointerInput(true) {
            detectDragGestures { change, dragAmount ->
                change.consume()  // Указатель мыши "захватывается"
                // Обновляем положение элемента
                node.offset = Offset(offset.x + dragAmount.x, offset.y + dragAmount.y)
                offset = node.offset
            }

        }
        .pointerInput(false) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val cursorOffset = event.changes.first().position
                    if (event.type == PointerEventType.Press) {
                        if (event.buttons.isSecondaryPressed) {
                            menuOffset = IntOffset(cursorOffset.x.toInt(), cursorOffset.y.toInt())
                            showMenu = true
                        } else {
                            // Скрываем меню при любом другом клике
                            showMenu = false
                        }
//                        if (event.buttons.isPrimaryPressed && model.isCreateLine.value) {
//                            model.links.value = model.links.value
//                                .plus(Link(model.startNode.value, node.id))
//                                .toMutableList()
//                            model.isCreateLine.value = false
//                        }
                    }
                }
            }
        }
        .border(width = Dp.Hairline, color = color, shape = RectangleShape)

    ) {
        Text(text = "Drag me")
        if (showMenu) {
            ContextMenu(menuOffset, nodeId, model) { showMenu = false }
        }
    }
}