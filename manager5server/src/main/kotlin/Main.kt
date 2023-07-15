import library.cndatabase.CnDatabase
import library.cnnavigation.interceptor.CnJsonInterceptor
import library.cnnavigation.interceptor.CnNavigationInterceptor
import library.cnserver.CnServer

fun main(args: Array<String>) {
    if (createDatabase()) {
        if (openDatabase()) {
            openServer()
        }
    }
}

private fun createDatabase(): Boolean {
    return CnDatabase.create(
        "jdbc:jtds:sqlserver://localhost;encrypt=true;trustServerCertificate=true",
        "sa",
        "132456",
        "glass"
    )
}

private fun openDatabase(): Boolean {
    val session = CnDatabase.open("src/main/resources/hibernate.cfg.xml")
    if (session != null) {
        App.session = session
        return true
    }
    return false
}

private fun openServer() {
    App.server = CnServer()
        .setPort(App.port)
        .addInterceptor(CnJsonInterceptor())
        .addInterceptor(CnNavigationInterceptor())
        .open()
}
