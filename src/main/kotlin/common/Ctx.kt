package common

import board.GraphTempFileDataSource
import common.Settings.Bootstrap
import common.json.OffsetSerializer
import common.json.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.nio.file.Files
import java.nio.file.Path

object Ctx {
    /**
     * CoroutineScope который живёт пока приложение не закроется.
     * При закрытии приложение, вызываться метод close(), отменяющий все незавершённые Coroutine
     */
    lateinit var coroutineScope : AppCoroutineScope
    lateinit var json: Json
    lateinit var settings: Settings

    lateinit var graphDataSource: GraphTempFileDataSource
}

@Serializable
class Settings {
    lateinit var bootstrap: Bootstrap

    @Serializable
    class Bootstrap {
        lateinit var appName: String
        lateinit var appTempDirPath: Path
    }
}

@DslMarker
annotation class AppDSL

@AppDSL
fun Ctx.settings(block: Settings.() -> Unit) =
    run { this.settings = Settings().apply(block) }

@AppDSL
fun Settings.bootstrap(block: Bootstrap.() -> Unit) =
    run { this.bootstrap = Bootstrap().apply(block) }


fun Ctx.init() {
    coroutineScope = AppCoroutineScope

    settings {
        bootstrap {
            appName = "bot-canvas"
            appTempDirPath = Path.of(System.getProperty("java.io.tmpdir"), appName)
                .also { if (!Files.exists(it)) Files.createDirectory(it) }
        }
    }

    json = Json {
        serializersModule = SerializersModule {
            contextual(UUIDSerializer)
            contextual(OffsetSerializer)
        }
    }
    graphDataSource = GraphTempFileDataSource(json, settings.bootstrap.appTempDirPath)
        .also { it.loadFromFile()  }

}

