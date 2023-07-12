package library.cnnavigation.model

import kotlin.jvm.Throws

data class CnRequest(
    val id: String?,
    val method: CnMethod?,
    var endpoint: String?,
    var params: Map<String, Any?>?,
    val body: Any?,
) {

    @Throws(Exception::class)
    fun validate() {
        if (id.isNullOrEmpty()) {
            throw Exception("Missing request id")
        }

        if (method == null) {
            throw Exception("Missing request method")
        }

        endpoint = endpoint ?: ""
        params = params ?: mapOf()
    }
}
