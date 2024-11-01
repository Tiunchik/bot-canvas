package board

import board.dto.Graph
import board.dto.Link
import board.dto.Node
import common.Ctx
import common.compose.launchIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import java.util.*

val hardcodeSelectedGraphUUID = UUID.fromString("777e5184-84e0-452a-9733-22c865b57fb3")

class GraphTempFileDataSource(
    private val json: Json = Ctx.json,
    private val appTempDirPath: Path // обычно = C:\Users\user\AppData\Local\Temp\bot-canvas
) {

    private val graphsFile by lazy {
        appTempDirPath.resolve("graphs.json").toFile()
            .also { if (!it.exists()) it.createNewFile() }
    }
    private val allGraphsStore = MutableStateFlow<Map<UUID, Graph>>(mutableMapOf())
    val allGraphs = allGraphsStore.asStateFlow()

    init {
        // в случаем когда файла ещё нет, ты хардкодим что у нас есть Изначальный граф
        if (allGraphsStore.value.isEmpty()) allGraphsStore.update {
            mutableMapOf(
                hardcodeSelectedGraphUUID to Graph(hardcodeSelectedGraphUUID)
            )
        }
    }

    fun loadStateFromFile() = launchIO("load file graph.json") {
        graphsFile.readText().let { fileText ->
            if (fileText.isBlank()) return@launchIO
            else json.decodeFromString<MutableMap<UUID, Graph>>(fileText)
                .also { graphs ->
                    println("load from file = ${graphs.values.joinToString()}")
                    graphs
                        .map { (_, graph) -> createObjectReferencesForLinks(graph) }
                        .forEach { graphs[it.uuid] = it }
                    allGraphsStore.update { graphs }
                }
        }
    }

    suspend fun saveStateToFile() = launchIO("save file graph.json") {
        println("load from file = ${allGraphs.value.values.joinToString()}")
        json.encodeToString(allGraphs.value).let { graphsFile.writeText(it) }
    }.join()

    // TODO move to Repo Layer

    operator fun get(uuid: UUID): Result<Graph> = allGraphsStore.value[uuid].let {
        if (it == null) Result.failure(RuntimeException("нет такого графа! graph.uuid=$uuid"))
        else Result.success(it)
    }

    fun getOrThrow(graphUUID: UUID): Graph = this[graphUUID].fold(
        onFailure = { throw it.also { it.printStackTrace() } },
        onSuccess = { it })

    // TODO move to Domain Layer

    fun addNode(graphUUID: UUID, node: Node) {
        getOrThrow(graphUUID).let { selectedGraph ->
            val newVersion = allGraphsStore.value +
                    (graphUUID to selectedGraph.copy(nodes = selectedGraph.nodes + node))
            allGraphsStore.update { newVersion.toMutableMap() }
        }
    }

    fun addLink(graphUUID: UUID, src: Node, trg: Node): Result<Link> = this[graphUUID].fold(
        onFailure = { Result.failure(it) },
        onSuccess = { graph ->
            if (src.id == trg.id) failResult<Link> { "Невозможно создать связь к самому себе!" }
            if (graph.containsLink(src.id, trg.id))
                return failResult { "Невозможно создать связь которая уже существует!" }

            Link(src, trg).let { link ->
                updateGraph(graph.copy(links = graph.links + link))
                Result.success(link)
            }
        })


    /* PRIVATE API */


    private fun updateGraph(newStateVersion: Graph) =
        allGraphsStore.update { (allGraphsStore.value + (newStateVersion.uuid to newStateVersion)) }

    /**
     * При save/load все объекты пересоздаются в коде, то kotlin serialize ничего не знает, об object reference
     * Таким образом и создаёт по 2 экземпляра Node.
     * Внутри [Graph.nodes] и внутри [Link.startNode] & [Link.endNode]
     * Мы затираем значения [Link.startNode] & [Link.endNode], значение из [Graph.nodes]
     */
    private fun createObjectReferencesForLinks(graph: Graph): Graph {
        return graph.copy(
            links = graph.links.map {
                Link(
                    startNode = graph.nodes.find { node -> node.id == it.startNode.id }!!,
                    endNode = graph.nodes.find { node -> node.id == it.endNode.id }!!,
                )
            }
        )
    }
}

fun <T> failResult(failMsgInit: () -> String) = Result.failure<T>(BusinessException(failMsgInit().also { println(it) }))

class BusinessException(msg: String, cause: Throwable? = null) : RuntimeException(msg, cause)