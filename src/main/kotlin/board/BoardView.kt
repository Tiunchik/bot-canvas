package board

import common.Ctx
import common.ViewModel
import dto.Node
import kotlinx.coroutines.flow.*
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
    private val graphDataSource: GraphTempFileDataSource = Ctx.graphDataSource
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(
        BoardUIState(graph = graphDataSource.getOrThrow(selectedGraphUUID))
    )
    val uiState = mutableUiState.stateIn(coroutineScope, SharingStarted.Eagerly, mutableUiState.value)
    val allNodes: List<Node> get() = this.uiState.value.graph.nodes


    /** Подписка UiState на изменение в DataSource state
     * Когда данные в DataSource обновляются, проверяем если ли наш [Graph] с таким [selectedGraphUUID],
     * если да то, прокидываем обновление в наш поток
     * */
    init {
        coroutineScope.launch {
            graphDataSource.allGraphs
                .stateIn(
                    scope = coroutineScope,
                    started = SharingStarted.Eagerly,
                    initialValue = mutableMapOf()
                )
                .collect { graphs ->
                    (graphs[selectedGraphUUID] ?: return@collect)
                        .let { updGraph -> mutableUiState.update { it.copy(graph = updGraph) } }
                }
        }
    }

    fun addNode(node: Node) = coroutineScope.launch { graphDataSource.addNode(selectedGraphUUID, node) }

}