package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import board.BoardView
import board.dto.Node
import kotlinx.coroutines.coroutineScope

@Composable
fun DraggableNode(modifier: Modifier, view: BoardView, node: Node) {
    // Переменная для управления отображением меню
    var showMenu by remember { mutableStateOf(false) }
    // Переменная для хранения позиции клика
    var menuOffset by remember { mutableStateOf(IntOffset.Zero) }

    Box(modifier = modifier
        .offset { IntOffset(node.offset.x.toInt(), node.offset.y.toInt()) }
        .width(node.width.dp)
        .height(node.height.dp)
        /* жесть перетаскивание узла */
        .pointerInput(node) {
            coroutineScope {
                detectDragGestures { change, dragAmount ->
                    change.consume()  // Указатель мыши "захватывается"
                    // Обновляем положение элемента
                    node.offset = Offset(x = node.offset.x + dragAmount.x, y = node.offset.y + dragAmount.y)
                }
            }
        }
        /* жесть Открыть контекстное меню при нажатии ПКМ */
        /* отслеживаем Падение tempArrow на это узел (создать связь, цель этот узел) */
        .pointerInput(node) {
            coroutineScope {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val cursorOffset = event.changes.first().position

                        if (event.type == PointerEventType.Press) {

                            // Нажатие ПКМ - открыть контекстное меню
                            if (event.buttons.isSecondaryPressed) {
                                menuOffset = IntOffset(cursorOffset.x.toInt(), cursorOffset.y.toInt())
                                showMenu = true
                            } else {
                                showMenu = false // Скрываем меню при любом другом клике
                            }

                            // Нажатие ЛКМ - Опустить рисуемую стрелку на Это блок (сделать связь между блоками)
                            if (event.buttons.isPrimaryPressed && view.tempArrow.isDraw) {
                                view.addLink(view.tempArrow.startNode!!, node)
                            }
                        }
                    }
                }
            }
        }
        .border(width = Dp.Hairline, color = view.defaultBoxColor, shape = RectangleShape)
        .background(Color.White)
    ) {
        Text(text = "Drag me #${node.idText}")
        if (showMenu) NodeContextMenu(menuOffset, node, view) { showMenu = false }
    }
}
