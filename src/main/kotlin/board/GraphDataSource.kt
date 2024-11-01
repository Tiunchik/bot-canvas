package board

import common.Ctx
import common.compose.launchIO
import dto.Node
import kotlinx.coroutines.flow.*
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

    fun loadStateFromFile() = launchIO("load file graph.json") {
        graphsFile.readText().let {
            if (it.isBlank()) return@launchIO
            else json.decodeFromString<MutableMap<UUID, Graph>>(it)
                .let { loaded -> allGraphsStore.update { loaded } }
        }
    }

    fun saveStateToFile() = launchIO("save file graph.json") {
        json.encodeToString(allGraphsStore.value).let { graphsFile.writeText(it) }
    }


    private val allGraphsStore = MutableStateFlow<MutableMap<UUID, Graph>>(mutableMapOf())
    val allGraphs = allGraphsStore.asStateFlow()

    init {
        // в случаем когда файла ещё нет, ты хардкодим что у нас есть Изначальный граф
        if (allGraphsStore.value.isEmpty()) allGraphsStore.update {
            mutableMapOf(
                hardcodeSelectedGraphUUID to Graph(hardcodeSelectedGraphUUID)
            )
        }
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
            val newVersion = allGraphsStore.value +
                    (graphUUID to selectedGraph.copy(nodes = selectedGraph.nodes + node))
            allGraphsStore.update { newVersion.toMutableMap() }
        }
    }
}