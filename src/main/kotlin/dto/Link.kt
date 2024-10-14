package dto

import androidx.compose.ui.geometry.Offset
import java.util.UUID

data class Link(
    var startNode: UUID,
    var endNode: UUID
)