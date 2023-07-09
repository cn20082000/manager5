package library.cnnavigation.model

import kotlin.jvm.Throws

data class CnResponse(
    val id: String?,
    val method: CnMethod?,
    var endpoint: String?,
    var params: Map<String, String>?,
    var header: Map<String, String>?,
    val data: Any?,
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
        header = header ?: mapOf()

        if (code == null) {
            throw Exception("Missing response code")
        }
    }
}
