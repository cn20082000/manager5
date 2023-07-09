import library.cnserver.CnServer
import library.cnnavigation.CnNavigationInterceptor

fun main(args: Array<String>) {
    val server = CnServer()
        .setPort(6666)
        .setInterceptor(CnNavigationInterceptor())
        .open()
//    server.close()
}