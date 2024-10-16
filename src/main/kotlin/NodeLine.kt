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
import dto.Node


@Composable
fun NodeLine(firstNode: Node, secondNode: Node) {

    val startPosition = firstNode.offset
    Canvas(modifier = Modifier
        .zIndex(LINE_LEVEL)
    ) {
        drawLine(color = Color.Black, firstNode.getNodeCenter(), secondNode.getNodeCenter())
    }
}