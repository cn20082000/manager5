package library.cnnavigation.model

data class CnResponse(
    val id: String?,
    val method: CnMethod?,
    var endpoint: String?,
    var params: Map<String, Any?>?,
    val body: Any?,
    val code: Int?,
) {

    @Throws(Exception::class)
    fun validate() {
        if (id.isNullOrEmpty()) {
            throw Exception("Missing response id")
        }

        if (method == null) {
            throw Exception("Missing response method")
        }

        endpoint = endpoint ?: ""
        params = params ?: mapOf()

        if (code == null) {
            throw Exception("Missing response code")
        }
    }

    companion object {
        fun ok(data: Any?): CnResponse = CnResponse(null, null, null, null, data, 200)
    }
}
