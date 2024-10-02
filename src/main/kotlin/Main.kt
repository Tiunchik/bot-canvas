import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeCanvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.TextLine


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}





@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
        Surface {
            nodes.forEach { DraggableNode((it.position)) }
        }
    }
}


// Данные для графа
data class Node(val id: Int, val position: Offset)
data class Edge(val startNode: Node, val endNode: Node)

// Пример данных для узлов и рёбер
val nodes = listOf(
    Node(1, Offset(100f, 100f)),
    Node(2, Offset(300f, 100f)),
    Node(3, Offset(200f, 300f))
)

val edges = listOf(
    Edge(nodes[0], nodes[1]),
    Edge(nodes[1], nodes[2]),
    Edge(nodes[2], nodes[0])
)



@Composable
fun GraphView(nodes: List<Node>, edges: List<Edge>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Рисуем рёбра
        for (edge in edges) {
            drawEdge(edge)
        }
        // Рисуем узлы
        for (node in nodes) {
            drawNode(node)
        }
    }
}

// Функция для рисования узла
fun DrawScope.drawNode(node: Node) {
    drawCircle(
        color = Color.Blue,
        radius = 20f,
        center = node.position
    )
}

// Функция для рисования рёбер
fun DrawScope.drawEdge(edge: Edge) {
    drawLine(
        color = Color.Gray,
        start = edge.startNode.position,
        end = edge.endNode.position,
        strokeWidth = 4f
    )
}