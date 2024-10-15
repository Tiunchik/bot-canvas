import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import dto.ViewModel
import java.util.UUID

@Composable
fun ContextMenu(offset: IntOffset, nodeId: UUID, model: ViewModel, onDismissRequest: () -> Unit) {
    Popup(
        offset = offset,
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier.size(150.dp).background(Color.White).shadow(4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                MenuItem("Create onText link") {
                    model.startNode.value = nodeId
                    model.isCreateLine.value = true
                    println("захватили ноду")
                    onDismissRequest.invoke()
                }
                MenuItem("Action 2")
                MenuItem("Action 3")
            }
        }
    }
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp)
            .clickable(enabled = true, onClick = { onClick.invoke() } )
    ) {
        BasicText(text)
    }
}