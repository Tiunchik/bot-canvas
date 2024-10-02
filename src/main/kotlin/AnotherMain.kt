import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.*
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize

@Composable
fun MainScreen() {
    // Используем Box для разделения верхней строки меню и остальной части экрана
    Box(modifier = Modifier.fillMaxSize()) {
        // Верхнее меню
        TopMenu()

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
            LeftCanvas(modifier = Modifier
                .weight(0.80f) // 80% ширины экрана
                .fillMaxHeight()
            )

            // Правая часть - меню с прокруткой (20% ширины экрана, минимум 100px)
            RightMenu(modifier = Modifier
                .fillMaxHeight()
                .widthIn(min = 300.dp) // Минимальная ширина 100 пикселей
                .weight(0.20f) // 5% ширины экрана
            )
        }
    }
}

// Компонент для верхнего меню
@Composable
fun TopMenu() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Кнопка 1
        Button(onClick = { /* Логика кнопки 1 */ }) {
            Text("Кнопка 1")
        }

        // Кнопка 2
        Button(onClick = { /* Логика кнопки 2 */ }) {
            Text("Кнопка 2")
        }

        // Кнопка 3
        Button(onClick = { /* Логика кнопки 3 */ }) {
            Text("Кнопка 3")
        }
    }
}

// Компонент для рисования на Canvas
@Composable
fun LeftCanvas(modifier: Modifier = Modifier) {
    Surface(modifier) {
        DraggableNode(Offset(0f, 0f))
    }
}

@Composable
fun DraggableNode(initialPosition: Offset) {
    var position by remember { mutableStateOf(initialPosition) }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                // Обновляем позицию узла на основе перемещения мыши
                position = Offset(position.x + dragAmount.x, position.y + dragAmount.y)
                change.consume() // Отметить, что событие обработано
            }
        }) {
        drawNode(Node(1, position))
    }
}

// Компонент для правого меню с прокруткой
@Composable
fun RightMenu(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
            .background(Color.DarkGray)
    ) {
        // Пример элементов меню
        repeat(20) {
            Text("Пункт меню $it", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
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
