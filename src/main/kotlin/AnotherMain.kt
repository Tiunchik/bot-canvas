import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dto.CurrentLink
import dto.Node
import dto.ViewModel
import java.util.UUID


@Composable
fun MainScreen(model: ViewModel = remember { ViewModel() }) {
    var boxColor: Color by remember { mutableStateOf(Color.Black) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Верхнее меню
        TopMenu(model) {
            boxColor = it
        }

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
                boxColor,
                model
            )

            // Правая часть - меню с прокруткой (20% ширины экрана, минимум 100px)
            RightMenu(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(min = 300.dp) // Минимальная ширина 100 пикселей
                    .weight(0.20f),
                onMenuItemClick = { model.nodes.value = model.nodes.value.plus(it).toMutableList() }
            )
        }
    }
}


// Компонент для верхнего меню
@Preview
@Composable
fun TopMenu(model: ViewModel, block: (Color) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Кнопка 1
        Button(onClick = { block.invoke(Color.Red) }) {
            Text("Красный")
        }

        // Кнопка 2
        Button(onClick = { block.invoke(Color.Black) }) {
            Text("Чёрный")
        }

        // Кнопка 3
        Button(onClick = { block.invoke(Color.Green) }) {
            Text("Зелёный")
        }

        Button(onClick = {
            model.nodes.value.forEach { println("node ${it.id} offset - ${it.offset.x}=${it.offset.y}") }
            model.links.value.forEach { println("links - ${it.startNode}=${it.endNode}") }
        }
        ) {
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

@Composable
fun App2() {
    MaterialTheme {
        MainScreen()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App2()
    }
}
