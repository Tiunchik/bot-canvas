package view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import dto.Arrow
import dto.Link
import dto.Node
import java.util.UUID

class ApplicationState {
    var nodes by mutableStateOf(mutableListOf<Node>())
    var links by mutableStateOf(mutableListOf<Link>())

    var boxColor by mutableStateOf(Color.Black)
    var tempArrow by mutableStateOf(Arrow())


    fun containsLink(startNodeId: UUID, endNodeId: UUID): Boolean =
        links.find { it.startNode.id == startNodeId && it.endNode.id == endNodeId } != null


    /**
     * логика создания связи между нодами, когда связь от первой ноды дотянули до второй
     * и нажали левой клавишей на первой ноде
     */
    fun addLinkTo(node: Node) {
        // Если мы пытаемся делать связь сами с собой -> выход
        if (this.tempArrow.startNodeId == node.id) {
            println("нельзя создать связь к самому себе!")
            return
        }
        // Эти 2 ноды уже имеют такую связь -> выход
        if (this.containsLink(tempArrow.startNodeId, node.id)) {
            println("нельзя создать связь которая уже существует!")
            return
        }

        tempArrow.apply {
            endNode = node
            isDraw = false
        }
        links += tempArrow.toLink()

        println("создали связь")

    }
}


