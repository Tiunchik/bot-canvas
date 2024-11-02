package board.dto

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    var startNode: Node,
    var endNode: Node
) {
    enum class Direction {IN, OUT}
}