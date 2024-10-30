package board

import common.Ctx
import common.ViewModel
import dto.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

data class BoardUIState(
    val graph: Graph
)

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
    val graphDataSource: GraphTempFileDataSource = Ctx.graphDataSource
) : ViewModel() {

    val mutableState = MutableStateFlow(
        BoardUIState(graph = graphDataSource.getOrThrow(selectedGraphUUID))
    )
//    val uiSate = mutableState
//        .stateIn(
//            Ctx.coroutineScope,
//            SharingStarted.Eagerly,
//            BoardUIState(graph = graphDataSource.getOrThrow(selectedGraphUUID))
//        )

    init {
        /* подписка UiState на изменение в DataSource state */
        viewModelScope.launch {
            graphDataSource.allGraphs
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    mutableMapOf()
                ).collect { graphs ->
                    (graphs[selectedGraphUUID] ?: return@collect)
                        .let { updGraph -> mutableState.update { it.copy(graph = updGraph) } }
                }
        }
    }

    fun addNode(node: Node) = viewModelScope.launch { graphDataSource.addNode(selectedGraphUUID, node) }

    // TODO : MVVM get MutableState<List<Node>>
    fun getAllNodes(): List<Node> = mutableState.value.graph.nodes

}