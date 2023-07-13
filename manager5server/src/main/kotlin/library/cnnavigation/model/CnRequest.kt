package library.cnnavigation.model

data class CnRequest(
    val id: String?,
    val method: CnMethod?,
    var endpoint: String?,
    var headers: Map<String, Any?>?,
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
        headers = headers ?: mapOf()
        params = params ?: mapOf()
    }
}
