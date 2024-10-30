package dto

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable

data class Node(
    @Contextual var id: UUID = UUID.randomUUID(),
    @Contextual var offset: Offset = Offset(0f, 0f),
    @Contextual var color: Color = Color.Black,
    var width: Int = 80,
    var height: Int = 40
) {

    val center: Offset get() = Offset(x = offset.x + width / 2, y = offset.y + height / 2)

}
