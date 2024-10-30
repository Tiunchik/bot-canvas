package board

import dto.Node
import java.util.UUID

class GraphRepo (
    val dataSource: GraphTempFileDataSource
){
    fun addNode(graphUUID: UUID, node: Node) {

    }
}