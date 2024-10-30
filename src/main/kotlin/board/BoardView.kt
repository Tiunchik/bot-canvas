package board

import common.Ctx
import common.ViewModel
import dto.Node
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

//class BoardStateRepo

data class BoardUIState(
    val graph: Graph
//    val nodes: List<Node> = emptyList()

)

//fun GraphView(state : GraphUIState) = BoardView(mutableStateOf(state))
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
//    val state : MutableState<BoardUIState>,

//    preSelectedGraphUUID: UUID?,
    val selectedGraphUUID: UUID,
    val graphDataSource: GraphTempFileDataSource = Ctx.graphDataSource
) : ViewModel() {
    //    var selectedGraphUUID: UUID = preSelectedGraphUUID ?: throw RuntimeException("ква-ква-ква...")
//        set(value) {
//            graphDataSource[value].fold(
//                onFailure = { throw it.also { it.printStackTrace() } },
//                onSuccess = { state.update { old -> old.copy(nodes = it.nodes) } })
//            field = value
//        }
//    val state: MutableStateFlow<BoardUIState> = MutableStateFlow(
    val mutableState = MutableStateFlow(
        BoardUIState(graph = graphDataSource.getOrThrow(selectedGraphUUID))
    )
    val uiSate = mutableState
//        .stateIn(
//            Ctx.coroutineScope,
//            SharingStarted.Eagerly,
//            BoardUIState(graph = graphDataSource.getOrThrow(selectedGraphUUID))
//        )

    init {
        viewModelScope.launch {
//        Ctx.launch(Dispatchers.IO) {
//            if (preSelectedGraphUUID != null) {
//                selectedGraphUUID = preSelectedGraphUUID

//                graphDataSource[preSelectedGraphUUID].fold(
//                    onFailure = { throw it.also { it.printStackTrace() } },
//                    onSuccess = { state.update { old -> old.copy(nodes = it.nodes) } })
//            }


            /* подписка UiState на изменение в DataSource state */
            graphDataSource.allGraphs
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    mutableMapOf()
                ).collect { graphs ->
                    println("subscribe1")
                    (graphs[selectedGraphUUID]
//                        ?: throw RuntimeException("ква-ква-ква...")) // Если такое произошло, значит Граф был как-то удалён...
                        ?: return@collect)
                        .let { updGraph -> mutableState.update { it.copy(graph = updGraph) } }
                    println("subscribe2")
                }
        }

    }

    fun addNode(node: Node) = viewModelScope.launch {
        println("addNode1")
        graphDataSource.addNode(selectedGraphUUID, node)
        println("addNode2")
    }

    fun getAllNodes(): List<Node> {
//        val rsl = viewModelScope.async {
        return mutableState.value.graph.nodes

//        }.await()
    }


//    private fun MutableStateFlow<GraphUIState>.updateStateBy(graph: Graph) {
//        this.up
//    }

    // Business logic //
    // graph -> addNode(node)
    // graph -> node -> edit (content, position)
    // graph -> addLinkBetween(start: Node, end: Node)

}