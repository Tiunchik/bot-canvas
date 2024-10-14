package dto

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import java.util.UUID

class ViewModel {
    var nodes = mutableStateOf(mutableListOf<Node>())
    var links = mutableStateOf(mutableListOf<Link>())

    var startNode = mutableStateOf(UUID.randomUUID())
    var isCreateLine = mutableStateOf(false)
    var startPosition = mutableStateOf(Offset(0f, 0f))

    fun updateStartPosition(offset: Offset) {
        println("Start position - ${offset}")
        startPosition.value = offset
    }


    fun updateStartPosition(x: Int, y: Int) = updateStartPosition(Offset(x.toFloat(), y.toFloat()))

}
