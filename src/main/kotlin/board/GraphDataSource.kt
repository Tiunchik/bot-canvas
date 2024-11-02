package board

import board.dto.Graph
import board.dto.Link
import board.dto.Node
import common.Ctx
import common.compose.launchIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
//                    println("load from file = ${graphs.values.joinToString()}")
                    graphs
                        .map { (_, graph) -> createObjectReferencesForLinks(graph) }
                        .forEach { graphs[it.uuid] = it }
                    allGraphsStore.update { graphs }
                }
        }
    }

    suspend fun saveStateToFile() = launchIO("save file graph.json") {
//        println("load from file = ${allGraphs.value.values.joinToString()}")
        json.encodeToString(allGraphs.value).let { graphsFile.writeText(it) }
    }.join()

    // TODO move to Repo Layer

    operator fun get(uuid: UUID): Result<Graph> = allGraphsStore.value[uuid]
        ?.let { Result.success(it) } ?: failResult { "Не существует графа с таким uuid! graph.uuid=$uuid" }

    fun getOrThrow(graphUUID: UUID): Graph = this[graphUUID].fold(
        onFailure = { throw it.also { it.printStackTrace() } },
        onSuccess = { it })

    /**
     * Если граф с таким UUID есть, то ждём его изменённую копию и пушим в поток
     */
    inline fun getGraphForUpdate(graphUUID: UUID, graphCopyProvider: Graph.() -> Graph): Result<Unit> =
        this[graphUUID].fold(
            onFailure = { exception -> Result.failure(exception) },
            onSuccess = { graph -> pushUpdateGraph(graphCopyProvider(graph)); Result.success(Unit) })

    /**
     * Если граф с таким UUID есть, то передаём его для бизнес логики, что может вернуть другой тип данных или exception
     * В таком случаи, нужно самим вызывать [pushUpdateGraph] при валидных изменениях [Graph]
     */
    inline fun <T> getGraphForResult(graphUUID: UUID, resultMapper: Graph.() -> Result<T>) = this[graphUUID].fold(
        onFailure = { exception -> Result.failure(exception) },
        onSuccess = { graph -> resultMapper(graph) })

    fun subscribeToGraphChange(coroutineScope: CoroutineScope, graphUUID: UUID, onChange: Graph.() -> Unit) {
        coroutineScope.launch {
            allGraphs.stateIn(coroutineScope, SharingStarted.Eagerly, mapOf())
                .collect { graphs -> onChange((graphs[graphUUID] ?: return@collect)) }
        }
    }


    // TODO move to Domain Layer

    fun addNode(graphUUID: UUID, node: Node) = getGraphForUpdate(graphUUID) { copy(nodes = nodes + node) }

    fun addLink(graphUUID: UUID, src: Node, trg: Node): Result<Link> = getGraphForResult(graphUUID) {
        if (src.id == trg.id) return failResult { "Невозможно создать связь к самому себе!" }
        if (containsLink(src.id, trg.id))
            return failResult { "Невозможно создать связь которая уже существует!" }
        Link(src, trg).let { link ->
            pushUpdateGraph(copy(links = links + link))
            return Result.success(link)
        }
    }


    // TODO: Debug !!! Плавающий баг!
    fun deleteAllLinks(graphUUID: UUID, node: Node, direction: Link.Direction) = getGraphForUpdate(graphUUID) {
        copy(
            links = links.filterNot {
                when (direction) {
                    Link.Direction.IN -> it.endNode.id == node.id
                    Link.Direction.OUT -> it.startNode.id == node.id
                }
            }
        )
    }


    // TODO: Debug !!! Плавающий баг!
    fun deleteNode(graphUUID: UUID, node: Node) = getGraphForUpdate(graphUUID) {
        copy(
            nodes = nodes.filter { it.id != node.id },
            links = links.filter { it.startNode.id != node.id && it.endNode.id != node.id }
//                links = graph.links.filterNot { it.startNode.id == node.id || it.endNode.id == node.id }
        )
    }

    /* PRIVATE API */

    fun pushUpdateGraph(newStateVersion: Graph) =
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

fun <T> fail(failMsgInit: () -> String) = BusinessException(failMsgInit().also { println(it) })
fun <T> failResult(failMsgInit: () -> String) = Result.failure<T>(BusinessException(failMsgInit().also { println(it) }))

class BusinessException(msg: String, cause: Throwable? = null) : RuntimeException(msg, cause)