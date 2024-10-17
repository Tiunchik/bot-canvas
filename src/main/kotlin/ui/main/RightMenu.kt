package ui.main

import SYSTEM_LEVEL
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dto.Node


// Компонент для правого меню с прокруткой
@Composable
fun RightMenu(modifier: Modifier = Modifier, onMenuItemClick: (Node) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .zIndex(SYSTEM_LEVEL)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .background(Color.DarkGray)
    ) {
        // Пример элементов меню
        repeat(5) {
            Button(onClick = { onMenuItemClick.invoke(Node()) }) {
                Text("Добавить узел")
            }
        }
    }
}