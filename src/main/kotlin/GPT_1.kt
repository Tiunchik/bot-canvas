import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingLine() {
    var startPoint by remember { mutableStateOf<Offset?>(null) }
    var endPoint by remember { mutableStateOf<Offset?>(null) }
    var tempEndPoint by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
//                detectDragGestures {  }

                // Обрабатываем клики для установки начальной и конечной позиции
                detectTapGestures(onTap = { position ->
                    if (startPoint == null) {
                        // Устанавливаем стартовую точку
                        startPoint = position
                    } else if (endPoint == null) {
                        // Устанавливаем конечную точку и фиксируем линию
                        endPoint = position
                    }
                })
            }
            .pointerMoveFilter(
                onMove = { movePosition ->
                    // Обновляем конечную точку при движении мыши
                    if (startPoint != null && endPoint == null) {
                        tempEndPoint = movePosition
                    }
                    true
                }
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Если стартовая точка установлена и есть временная конечная точка, рисуем линию
            startPoint?.let { start ->
                tempEndPoint?.let { tempEnd ->
                    drawLine(
                        color = Color.Blue,
                        start = start,
                        end = tempEnd,
                        strokeWidth = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }
            }

            // Рисуем зафиксированную линию, если есть конечная точка
            startPoint?.let { start ->
                endPoint?.let { end ->
                    drawLine(
                        color = Color.Red,
                        start = start,
                        end = end,
                        strokeWidth = 4f
                    )
                }
            }
        }
    }
}

// Запуск приложения на Desktop
fun main() = singleWindowApplication {
    MaterialTheme {
        DrawingLine()
    }
}

//
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.application
//
//@Composable
//fun DrawArrow() {
//    Canvas(modifier = Modifier.size(200.dp)) {
//        val path = Path().apply {
//            // Стартовая точка
//            moveTo(size.width / 4, size.height / 2)
//            // Линия вверх
//            lineTo(size.width * 3 / 4, size.height / 2)
//            // Стрелка вправо
//            lineTo(size.width * 2 / 3, size.height / 3)
//            moveTo(size.width * 3 / 4, size.height / 2)
//            lineTo(size.width * 2 / 3, size.height * 2 / 3)
//        }
//        drawPath(path, Color.Black , style = Stroke())
//    }
//}
//
//@Composable
//fun ArrowExample() {
//    Surface {
//        DrawArrow()
//    }
//}
//
//fun main() = application {
//    Window(onCloseRequest = ::exitApplication) {
//        MaterialTheme {
//            ArrowExample()
//        }
//    }
//}

