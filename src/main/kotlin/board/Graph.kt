package board

import dto.Link
import dto.Node
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Graph(
    @Contextual val uuid: UUID = UUID.randomUUID(),
    val nodes: List<Node> = listOf(),
    val links: List<Link> = listOf(),
)