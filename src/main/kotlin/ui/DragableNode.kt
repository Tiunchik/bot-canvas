package ui

import NODE_LEVEL
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dto.Node
import view.ApplicationState

@Composable
fun DraggableNode(appState: ApplicationState, node: Node, color: Color) {
    var offset by remember { mutableStateOf(node.offset) }

    // Переменная для управления отображением меню
    var showMenu by remember { mutableStateOf(false) }
    // Переменная для хранения позиции клика
    var menuOffset by remember { mutableStateOf(IntOffset.Zero) }

    Box(modifier = Modifier
        .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
        .zIndex(NODE_LEVEL)
        .width(node.width.dp)
        .height(node.height.dp)
        .pointerInput(true) {
            // ПЕРЕТАСКИВАНИЕ НОДЫ
            detectDragGestures { change, dragAmount ->
                change.consume()  // Указатель мыши "захватывается"
                // Обновляем положение элемента
                Offset(offset.x + dragAmount.x, offset.y + dragAmount.y).let {
                    node.offset = it
                    offset = it
                }
            }
        }
        .pointerInput(false) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val cursorOffset = event.changes.first().position
                    // Нажатие ПКМ - открыть контекстное меню
                    if (event.type == PointerEventType.Press) {
                        if (event.buttons.isSecondaryPressed) {
                            menuOffset = IntOffset(cursorOffset.x.toInt(), cursorOffset.y.toInt())
                            showMenu = true
                        } else {
                            showMenu = false // Скрываем меню при любом другом клике
                        }
                        if (event.buttons.isPrimaryPressed && appState.tempArrow.isDraw) {
                            appState.addLinkTo(node)
                        }
                    }
                }
            }
        }
        .border(width = Dp.Hairline, color = color, shape = RectangleShape)
        .background(Color.White)

    ) {
        Text(text = "Drag me")
        if (showMenu) NodeContextMenu(menuOffset, node, appState) { showMenu = false }
    }
}
