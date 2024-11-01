package board.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Graph(
    @Contextual val uuid: UUID = UUID.randomUUID(),
    val nodes: List<Node> = listOf(),
    val links: List<Link> = listOf(),
) {
    fun containsLink(startNodeId: UUID, endNodeId: UUID): Boolean =
        links.find { it.startNode.id == startNodeId && it.endNode.id == endNodeId } != null

}