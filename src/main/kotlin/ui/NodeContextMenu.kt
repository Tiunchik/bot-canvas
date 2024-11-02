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
import board.dto.Link
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
                MenuItem(modifier = Modifier.background(Color.Green), text = "Create onText link") {
                    view.startDrawingTempArrow(node)
                    onDismissRequest.invoke()
                }
                MenuItem(modifier = Modifier.background(Color.Red), text = "Delete Node") {
                    view.deleteNode(node)
                    onDismissRequest.invoke()
                }
                MenuItem(modifier = Modifier.background(Color.Red), text = "Delete all links IN") {
                    view.deleteAllLinks(node, Link.Direction.IN)
                    onDismissRequest.invoke()
                }
                MenuItem(modifier = Modifier.background(Color.Red), text = "Delete all links OUT") {
                    view.deleteAllLinks(node, Link.Direction.OUT)
                    onDismissRequest.invoke()
                }
            }
        }
    }
}

@Composable
fun MenuItem(modifier: Modifier = Modifier, text: String, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        BasicText(text)
    }
}