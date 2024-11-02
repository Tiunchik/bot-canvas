package board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import board.dto.Graph
import board.dto.Link
import board.dto.Node
import common.Ctx
import common.compose.ViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class BoardUIState(
    val graph: Graph,
)

data class ArrowUiState(
    var isDraw: Boolean = false,
    var startNode: Node? = null,
) {
    val startPoint: Offset get() = startNode?.center ?: throw RuntimeException("что-то не так")
}

/**
 * business API & ui state для Compose UI - для нашего типа холста где рисуем все детали Графа.
 *
 * Штука для работы с UI state, business API & ui state для Compose UI
 *
 * TODO Когда нужно отрисовать Другой Граф, то нужно создать новый объект [BoardView]
 * TODO пока живём с мыслью что Граф есть всегда, мб в будущем будет вариант с Пустым окном
 *      как в IDEA когда Выбранный Файл Отсутствует
 */
class BoardView(
    val selectedGraphUUID: UUID,
    private val graphDataSource: GraphTempFileDataSource = Ctx.graphDataSource
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(
        BoardUIState(graph = graphDataSource.getOrThrow(selectedGraphUUID))
    )
    val uiState = mutableUiState.stateIn(coroutineScope, SharingStarted.Eagerly, mutableUiState.value)
    val allNodes: List<Node> get() = this.uiState.value.graph.nodes
    val allLinks: List<Link> get() = this.uiState.value.graph.links
    var tempArrow by mutableStateOf(ArrowUiState(false, null))
    var defaultBoxColor by mutableStateOf(Color.Black)


    fun startDrawingTempArrow(startNode: Node) = run { tempArrow = ArrowUiState(isDraw = true, startNode = startNode) }
    fun stopDrawingTempArrow() = run { tempArrow = ArrowUiState(isDraw = false, startNode = null) }


    /** Подписка UiState на изменение в DataSource state
     * Когда данные в DataSource обновляются, проверяем если ли наш [Graph] с таким [selectedGraphUUID],
     * если да то, прокидываем обновление в наш поток
     * */
    init {
        graphDataSource.subscribeToGraphChange(coroutineScope, selectedGraphUUID) {
            mutableUiState.update { it.copy(graph = this) }
        }
    }

    // TODO: Оборачивать в OperationResult и в случаи не удачи, показывать error popup
    fun addNode(node: Node) = coroutineScope.launch { graphDataSource.addNode(selectedGraphUUID, node) }

    // TODO: Оборачивать в OperationResult и в случаи не удачи, показывать error popup
    fun addLink(src: Node, trg: Node) = coroutineScope.launch { graphDataSource.addLink(selectedGraphUUID, src, trg) }

    // TODO: Оборачивать в OperationResult и в случаи не удачи, показывать error popup
    fun deleteAllLinks(node: Node,  direction : Link.Direction)  = coroutineScope.launch { graphDataSource.deleteAllLinks(selectedGraphUUID, node,  direction)}

    // TODO: Оборачивать в OperationResult и в случаи не удачи, показывать error popup
    fun deleteNode(node: Node) = coroutineScope.launch { graphDataSource.deleteNode(selectedGraphUUID, node) }
}