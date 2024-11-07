package board.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.concurrent.atomic.AtomicLong

private val genId = AtomicLong(0)

// TODO: заменить Reference<Node> на UUID + ? delegate get from graph by uuid
@Serializable data class Link(
    @Transient val idText: Long = genId.getAndIncrement(),
    var startNode: Node,
    var endNode: Node
) {

    constructor(startNode: Node, endNode: Node) : this(
        idText = genId.getAndIncrement(),
        startNode = startNode,
        endNode = endNode
    )

    override fun toString(): String =
        "Link(idText=$idText, src.textId=${startNode.idText} -> trg.textId=${endNode.idText})"

    /* NESTED CLASSES */

    enum class Direction { IN, OUT }
}

fun List<Link>.toLogStr(): String =
    "links [${this.size}]" + this.joinToString(prefix = System.lineSeparator(), separator = System.lineSeparator())
