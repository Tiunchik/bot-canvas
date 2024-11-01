package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import board.BoardView
import board.dto.Node

@Composable
fun NodeContextMenu(
    offset: IntOffset,
    node: Node,
    view: BoardView,
    onDismissRequest: () -> Unit
) {
    Popup(
        offset = offset,
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier.size(150.dp).background(Color.White).shadow(4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                MenuItem("Create onText link") {
                    view.startDrawingTempArrow(node)
                    println("захватили ноду")
                    onDismissRequest.invoke() // метод выключения меню (Popup)
                }
                MenuItem("Action 2")
                MenuItem("Action 3")
            }
        }
    }
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        BasicText(text)
    }
}