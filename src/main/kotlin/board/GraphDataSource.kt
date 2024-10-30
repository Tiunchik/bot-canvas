package board

import common.Ctx
import common.launchIO
import dto.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import java.util.UUID

val hardcodeSelectedGraph = UUID.fromString("777e5184-84e0-452a-9733-22c865b57fb3")

class GraphTempFileDataSource(
    val json: Json = Ctx.json,
    val appTempDirPath: Path // обычно = C:\Users\user\AppData\Local\Temp\bot-canvas
) {

    private val graphsFile by lazy {
        appTempDirPath.resolve("graphs.json").toFile()
            .also { if (!it.exists()) it.createNewFile() }
    }

     fun loadFromFile() = launchIO("load file graph.json") {
        graphsFile.readText().let {
            if (it.isBlank()) return@launchIO
            else json.decodeFromString<MutableMap<UUID, Graph>>(it)
                .let { loaded -> allGraphsStore.update { loaded } }
        }
    }

    fun saveToFile() =  launchIO("save file graph.json") {
        json.encodeToString(allGraphsStore.value).let { graphsFile.writeText(it) }
    }


    private val allGraphsStore = MutableStateFlow<MutableMap<UUID, Graph>>(mutableMapOf())
    val allGraphs : Flow<MutableMap<UUID, Graph>> = allGraphsStore

    init {
        // в случаем когда файла ещё нет, ты хардкодим что у нас есть Изначальный граф
//        allGraphsStore.value[hardcodeSelectedGraph] = Graph(hardcodeSelectedGraph)
//        allGraphsStore.update { it }
        allGraphsStore.update { mutableMapOf(hardcodeSelectedGraph to Graph(hardcodeSelectedGraph)) }
    }


    // TODO move to Repo methods


    operator fun get(uuid: UUID): Result<Graph> = allGraphsStore.value[uuid].let {
        if (it == null) Result.failure(RuntimeException("нет такого графа! graph.uuid=$uuid"))
        else Result.success(it)
    }

    fun getOrThrow(graphUUID: UUID): Graph = this[graphUUID].fold(
        onFailure = { throw it.also { it.printStackTrace() } },
        onSuccess = { it })

    fun addNode(graphUUID: UUID, node: Node) {
        getOrThrow(graphUUID).let { selectedGraph ->
//            allGraphsStore.value[graphUUID] = selectedGraph.copy(
//                nodes = selectedGraph.nodes + node
//            )
            val newVersion = allGraphsStore.value +
                    (graphUUID to selectedGraph.copy(nodes = selectedGraph.nodes + node
            ))
            allGraphsStore.update { newVersion.toMutableMap() }
//            allGraphs.update { graphs ->
//
//                graphs
//            }
        }
    }
}