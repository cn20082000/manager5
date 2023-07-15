import library.cndatabase.CnDatabase
import library.cnnavigation.interceptor.CnJsonInterceptor
import library.cnnavigation.interceptor.CnNavigationInterceptor
import library.cnserver.CnServer

fun main(args: Array<String>) {
    if (CnDatabase.create(
            "jdbc:jtds:sqlserver://localhost;encrypt=true;trustServerCertificate=true",
            "sa",
            "132456",
            "glass"
        )
    ) {
        val server = CnServer()
            .setPort(6666)
            .addInterceptor(CnJsonInterceptor())
            .addInterceptor(CnNavigationInterceptor())
            .open()
//    server.close()
    }
}