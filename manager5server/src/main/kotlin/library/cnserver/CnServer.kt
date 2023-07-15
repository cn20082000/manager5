package library.cnserver

import java.net.Inet4Address
import java.net.ServerSocket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CnServer(
    private var port: Int = 55672,
    private var interceptors: MutableList<CnInterceptor> = mutableListOf()
) {
    private var server: ServerSocket? = null
    private var listening: CnServerListening? = null

    fun setPort(port: Int): CnServer {
        this.port = port
        return this
    }

    fun addInterceptor(interceptor: CnInterceptor): CnServer {
        interceptors.add(interceptor)
        return this
    }

    fun open(): CnServer {
        try {
            server = ServerSocket(port)
            cnServerLog("Server at ${Inet4Address.getLocalHost().hostAddress} and port $port opened.")

            listening = CnServerListening(server!!, interceptors)
            listening?.start()
        } catch (ex: Exception) {
            cnServerLog(ex)
        }
        return this
    }

    fun close() {
        try {
            listening?.terminate()
            server?.close()
            cnServerLog("Server at port $port closed.")
        } catch (ex: Exception) {
            cnServerLog(ex)
        }
    }

    companion object {
        val threadPool: ExecutorService = Executors.newFixedThreadPool(5)
    }
}