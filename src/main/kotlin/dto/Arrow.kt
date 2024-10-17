package dto

import androidx.compose.ui.geometry.Offset
import java.util.UUID

// TODO: возможно это view, а не dto ...
data class Arrow(
    var isDraw: Boolean = false,

    var startNode: Node? = null,
    var endNode: Node? = null,
) {
    val startNodeId: UUID get() = startNode?.id ?: throw RuntimeException("что-то не так")
//    val  endNodeId : UUID get() = endNode?.id  ?: throw RuntimeException("что-то не так")

    val startPoint: Offset get() = startNode?.center ?: throw RuntimeException("что-то не так")
    val endPoint: Offset get() = endNode?.center ?: throw RuntimeException("что-то не так")

    fun toLink(): Link = Link(
        startNode ?: throw RuntimeException("что-то не так"),
        endNode ?: throw RuntimeException("что-то не так")
    )
}