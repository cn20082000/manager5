package library.cnlog

class CnLog(
    private val name: String,
) {
    fun print(message: Any?) = println("$name: $message")

    fun print() = println("$name:")
}