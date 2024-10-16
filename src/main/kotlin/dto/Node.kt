package dto

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.UUID

data class Node(
    var id: UUID = UUID.randomUUID(),
    var offset: Offset = Offset(0f, 0f),
    var color: Color = Color.Black,
    var width: Int = 80,
    var height: Int = 40
) {

    fun getNodeCenter(): Offset {
        val x = offset.x + width/2
        val y = offset.y + height/2
        return Offset(x.toFloat(), y.toFloat())
    }

}
