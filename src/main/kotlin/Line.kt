import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun Line(startPoint: Offset, endPoint: Offset) {
    Canvas(modifier = Modifier
        .zIndex(NODE_LEVEL + 1f)) {
        drawLine(color = Color.Black, startPoint, endPoint)
    }
}

// TODO: имхо не очень хорошо плодить много Canvas для Каждой Линии...
@Composable
fun Arrow(startPoint: Offset,
          endPoint: Offset,) {
    Canvas(modifier = Modifier
        .zIndex(NODE_LEVEL + 1f)) {
        drawArrow(startPoint, endPoint, color = Color.Black, )
    }
}

// Функция для рисования стрелки
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawArrow(
    start: Offset,
    end: Offset,
    color: Color = Color.Black,
    strokeWidth: Float = 2.5f
) {
    // Рисуем основную линию стрелки
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth
    )

    // Рассчитываем угол наклона линии
    val arrowAngle = atan2(end.y - start.y, end.x - start.x)

    // Параметры для стрелки
    val arrowHeadLength = 20f // Длина головки стрелки
    val arrowHeadAngle = Math.toRadians(30.0) // Угол между основной линией и головкой стрелки

    // Вычисляем координаты для двух сторон головки стрелки
    val leftX = end.x - arrowHeadLength * cos(arrowAngle - arrowHeadAngle).toFloat()
    val leftY = end.y - arrowHeadLength * sin(arrowAngle - arrowHeadAngle).toFloat()

    val rightX = end.x - arrowHeadLength * cos(arrowAngle + arrowHeadAngle).toFloat()
    val rightY = end.y - arrowHeadLength * sin(arrowAngle + arrowHeadAngle).toFloat()

    // Рисуем две линии для головки стрелки
    drawLine(
        color = color,
        start = end,
        end = Offset(leftX, leftY),
        strokeWidth = strokeWidth
    )

    drawLine(
        color = color,
        start = end,
        end = Offset(rightX, rightY),
        strokeWidth = strokeWidth
    )
}