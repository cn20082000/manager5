import library.cnnavigation.interceptor.CnJsonInterceptor
import library.cnserver.CnServer
import library.cnnavigation.interceptor.CnNavigationInterceptor

fun main(args: Array<String>) {
    val server = CnServer()
        .setPort(6666)
        .addInterceptor(CnJsonInterceptor())
        .addInterceptor(CnNavigationInterceptor())
        .open()
//    server.close()
}