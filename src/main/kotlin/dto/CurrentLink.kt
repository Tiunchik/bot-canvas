package dto

import androidx.compose.ui.geometry.Offset
import java.util.UUID

data class CurrentLink(
    var start: Offset,
    var end: Offset,
    var startNode: UUID,
)