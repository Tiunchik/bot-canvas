package ui.main

import SYSTEM_LEVEL
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import board.BoardView
import view.ApplicationState

@Preview
@Composable
fun TopMenu(appState: ApplicationState, boardView: BoardView) {
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
        Button(onClick = { appState.boxColor = Color.Red }) { Text("Красный") }
        Button(onClick = { appState.boxColor = Color.Black }) { Text("Чёрный") }
        Button(onClick = { appState.boxColor = Color.Green }) { Text("Зелёный") }

        boardView.uiState.collectAsState()
        boardView.uiState.collectAsState()

        Button(onClick = boardView.executeAsync {
            println("=== RUN ===")
            appState.nodes.printState("app.nodes") { "node ${id} offset - ${offset.x}=${offset.y}" }
            appState.links.printState("app.links") { "links - ${startNode.id}=${endNode.id}" }
            boardView.allNodes.printState("board.nodes") { "board nodes = ${id} ${offset}" }
            println("=== END ===")
        }) {
            Text("Печать")
        }
    }
}

private fun <T> Collection<T>.printState(name: String, toStingMapper: T.() -> String) {
    print("""$name [${this.size}] """)
    if (this.isEmpty()) println()
    else this.forEach { println(it.toStingMapper()) }
}

