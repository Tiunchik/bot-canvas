package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DrawGrid(gridSize: Int, width: Float, height: Float) {
    // Используем Canvas для отрисовки сетки
    Canvas(modifier = Modifier.fillMaxSize()) {

        // Рисуем вертикальные линии
        for (x in 0..width.toInt() step gridSize) {
            drawLine(
                color = Color.Gray,
                start = androidx.compose.ui.geometry.Offset(x.toFloat(), 0f),
                end = androidx.compose.ui.geometry.Offset(x.toFloat(), height),
                strokeWidth = 1f
            )
        }

        // Рисуем горизонтальные линии
        for (y in 0..height.toInt() step gridSize) {
            drawLine(
                color = Color.Gray,
                start = androidx.compose.ui.geometry.Offset(0f, y.toFloat()),
                end = androidx.compose.ui.geometry.Offset(width, y.toFloat()),
                strokeWidth = 1f
            )
        }
    }
}