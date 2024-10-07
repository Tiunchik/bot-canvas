import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerButtons
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerKeyboardModifiers
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.isTertiaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toSize
import jdk.internal.org.jline.utils.InfoCmp.Capability.buttons

data class Line(val start: Offset, val end: Offset?)

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
            LeftCanvas(
                modifier = Modifier
                    .weight(0.80f) // 80% ширины экрана
                    .fillMaxHeight()
            )

            // Правая часть - меню с прокруткой (20% ширины экрана, минимум 100px)
            RightMenu(
                modifier = Modifier
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
    // Переменные для хранения начала и конца линии
    var lines by remember { mutableStateOf(mutableListOf<Line>()) }

    Box(
        modifier
            .fillMaxSize()
    )
    {
        DraggableNode(Offset(100f, 100f), lines)
        DraggableNode(Offset(400f, 100f), lines)
        Canvas(
            Modifier
                .background(color = Color.Blue)
        ) {
            lines.forEach { line ->
                if (line.end != null) {
                    drawLine(
                        color = Color.Black,
                        start = line.start,
                        end = line.end,
                        strokeWidth = 5f
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableNode(initialPosition: Offset, lines: MutableList<Line>) {
    var buttonsEvent by remember { mutableStateOf(PointerEvent) }
    var offset by remember { mutableStateOf(initialPosition) }
    var leftButton by remember { mutableStateOf(Boolean) }
    var rightButton by remember { mutableStateOf(Boolean) }

    Box(modifier = Modifier
        .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
        .pointerInput(Unit) {
            awaitPointerEventScope {
                val event = awaitPointerEvent()
                if (event.buttons.isSecondaryPressed) {
                    println("правая")
                }
            }
       }
        .border(width = Dp.Hairline, color = Color.Black, shape = RectangleShape)

    ) {
        Text("Drag me")

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
