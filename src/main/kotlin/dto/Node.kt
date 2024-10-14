package dto

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.UUID

data class Node(
    var id: UUID = UUID.randomUUID(),
    var offset: Offset = Offset(0f, 0f),
    var color: Color = Color.Black
)
