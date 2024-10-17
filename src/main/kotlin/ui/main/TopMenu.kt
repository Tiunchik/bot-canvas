package ui.main

import SYSTEM_LEVEL
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import view.ApplicationState

@Preview
@Composable
fun TopMenu(appState: ApplicationState) {

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

        Button(onClick = {
            appState.nodes.forEach { println("node ${it.id} offset - ${it.offset.x}=${it.offset.y}") }
            appState.links.forEach { println("links - ${it.startNode}=${it.endNode}") }
        }) {
            Text("Печать")
        }
    }
}

