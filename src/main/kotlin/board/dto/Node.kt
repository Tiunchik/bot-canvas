package board.dto

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*
import java.util.concurrent.atomic.AtomicLong

private val genId = AtomicLong(0)

// TODO: по идеи этот Object должен быть Immutable, но это не факт
@Serializable data class Node(
    @Transient val idText: Long = genId.getAndIncrement(),
    @Contextual var id: UUID = UUID.randomUUID(),
    // TODO: косяк, есть set value , нужно изолировать в BoardView.MoveNode
    @Contextual var offset: Offset = Offset(0f, 0f),
    @Contextual var color: Color = Color.Black,
    var width: Int = 100,
    var height: Int = 45
) {

    val center: Offset get() = Offset(x = offset.x + width / 2, y = offset.y + height / 2)

    override fun toString(): String = "Node(idText=$idText, center=$center)"

}

fun List<Node>.toLogStr(): String =
    "nodes [${this.size}]" + this.joinToString(prefix = System.lineSeparator(), separator = System.lineSeparator())

