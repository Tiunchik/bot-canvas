package dto

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.UUID

class ApplicationState {
    var nodes by mutableStateOf(mutableListOf<Node>())
    var links by mutableStateOf(mutableListOf<Link>())

    var startNode by mutableStateOf(UUID.randomUUID())
    var isCreateLine by mutableStateOf(false)
    var startPosition by mutableStateOf(Offset(0f, 0f))

//    var boxColor: MutableState<Color> by  mutableStateOf(Color.Black)
    var boxColor by mutableStateOf(Color.Black)
    var drawingArrow by mutableStateOf<DrawingArrow?>(null)


    fun updateStartPosition(offset: Offset) {
        println("Start position - ${offset}")
        startPosition = offset
    }

    fun updateStartPosition(x: Int, y: Int) = updateStartPosition(Offset(x.toFloat(), y.toFloat()))

}


data class DrawingArrow(
    var isDrawingArrow: Boolean,
    var startPoint: Offset,
    var endOffset: Offset
)
