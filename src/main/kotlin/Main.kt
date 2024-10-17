import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.zIndex
import dto.Node
import dto.ApplicationState

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    appState: ApplicationState = remember { ApplicationState() }
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Верхнее меню
        TopMenu(appState)

        Divider(
            modifier = Modifier
                .padding(58.dp),
            color = Color.Blue
        )

        // Нижняя часть экрана: Canvas слева и меню справа
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp) // Отступ для верхнего меню
        ) {
            // Левая часть - Canvas (80% ширины экрана)
            LeftCanvas(
                modifier = Modifier
                    .weight(0.80f) // 80% ширины экрана
                    .fillMaxHeight(),
                appState.boxColor,
                appState
            )

            // Правая часть - меню с прокруткой (20% ширины экрана, минимум 100px)
            RightMenu(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(min = 300.dp) // Минимальная ширина 100 пикселей
                    .weight(0.20f),
                onMenuItemClick = { appState.nodes = appState.nodes.plus(it).toMutableList() }
            )
        }
    }
}


// Компонент для верхнего меню
@Preview
@Composable
//fun TopMenu(model: ViewModel, block: MutableState<Color>) {
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


// Компонент для правого меню с прокруткой
@OptIn(ExperimentalFoundationApi::class)
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