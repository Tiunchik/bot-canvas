package ui.main

import SYSTEM_LEVEL
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import board.BoardView

@Preview
@Composable
fun TopMenu(view: BoardView) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(SYSTEM_LEVEL)
            .background(Color.DarkGray)
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { view.defaultBoxColor = Color.Red }) { Text("Красный") }
        Button(onClick = { view.defaultBoxColor = Color.Black }) { Text("Чёрный") }
        Button(onClick = { view.defaultBoxColor = Color.Green }) { Text("Зелёный") }

        Button(onClick = view.executeAsync {
            println("=== RUN LOG ===")
            view.allNodes.printState("nodes") { "$this" }
            view.allLinks.printState("links") { "$this | ${startNode.center}=${endNode.center}" }
            println("=== END LOG ===")
        }) {
            Text("Печать")
        }
    }
}

private fun <T> Collection<T>.printState(name: String, toStingMapper: T.() -> String) {
    println("""$name [${this.size}] """)
    this.forEach { println(it.toStingMapper()) }
}

