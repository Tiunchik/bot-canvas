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

    var boxColor by mutableStateOf(Color.Black)
    var tempArrow by mutableStateOf(Arrow())


    fun containsLink(startNodeId: UUID, endNodeId: UUID): Boolean =
        links.find { it.startNode == startNodeId && it.endNode == endNodeId } != null
}


data class Arrow(
    var isDraw: Boolean = false,

    var startPoint: Offset? = null,
    var endPoint: Offset? = null,

    var startNodeId: UUID? = null,
    var endNodeId: UUID? = null,
) {
    fun toLink(): Link = Link(
        startNodeId ?: throw RuntimeException("что-то не так"),
        endNodeId ?: throw RuntimeException("что-то не так")
    )
}
