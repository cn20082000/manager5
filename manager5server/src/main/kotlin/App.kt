import library.cnserver.CnServer
import org.hibernate.Session

object App {
    lateinit var server: CnServer
    lateinit var session: Session

    const val port = 6666
}