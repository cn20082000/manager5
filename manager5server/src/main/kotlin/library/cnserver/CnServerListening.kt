package library.cnserver

import library.cnthread.CnThread
import java.net.ServerSocket
import java.net.Socket

class CnServerListening(
    private val server: ServerSocket,
    private val interceptors: List<CnInterceptor>,
) : CnThread() {

    override fun loop() {
        try {
            val socket: Socket = server.accept()
            val process = CnServerProcess(socket, interceptors)
            process.start()
        } catch (ex: Exception) {
            cnServerLog(ex)
            terminate()
        }
    }
}