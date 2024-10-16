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


@Composable
fun Line(startPoint: Offset, endPoint: Offset) {
    Canvas(modifier = Modifier
        .zIndex(NODE_LEVEL + 1f)) {
        drawLine(color = Color.Black, startPoint, endPoint)
    }
}