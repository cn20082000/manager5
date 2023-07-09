package library.cnserver

import library.cnthread.CnThread
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

open class CnServerProcess(
    private val socket: Socket,
    private val interceptors: List<CnInterceptor>,
): CnThread() {

    private val printer: PrintWriter by lazy { PrintWriter(socket.getOutputStream(), true) }

    init {
        cnServerLog("New client (${socket.remoteSocketAddress}) connected")
    }

    private fun send(message: String) {
        printer.println(message)
    }

    override fun loop() {
        try {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val line = reader.readLine()
            if (line != null && interceptors.isNotEmpty()) {
                val result = interceptors.first().intercept(line, interceptors.subList(1, interceptors.size))
                if (result != null) {
                    send(result.toString())
                }
            }

            socket.getOutputStream().write(0)
        } catch (ex: Exception) {
            cnServerLog(ex)
            disconnect()
        }
    }

    private fun disconnect() {
        try {
            socket.close()
        } catch (ex: Exception) {
            cnServerLog(ex)
        }
        cnServerLog("Client (${socket.remoteSocketAddress}) disconnected")
        terminate()
    }
}