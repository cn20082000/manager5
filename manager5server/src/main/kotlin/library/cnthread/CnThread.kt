package library.cnthread

/**
 * Kể từ khi [start], khởi tạo một Thread mới chạy liên tục [loop] cho đến khi nó bị [terminate]
 */
abstract class CnThread {
    private var runnable: CnRunnable? = null
    private var thread: Thread? = null

    /**
     * Công việc cần thực hiện lặp lại
     */
    abstract fun loop()

    /**
     * Khởi tạo Thread mới và chạy liên tục [loop]
     */
    @Throws(IllegalThreadStateException::class)
    fun start() {
        runnable = CnRunnable(this)
        thread = Thread(runnable)
        thread?.start()
    }

    /**
     * Dừng [loop] và chờ cho đến khi Thread bị hủy hoàn toàn
     */
    @Throws(InterruptedException::class)
    fun terminate() {
        runnable?.terminate()
        thread?.join()
    }

    private class CnRunnable(
        private val thread: CnThread,
    ) : Runnable {
        @Volatile
        private var running = true

        override fun run() {
            while (running) {
                thread.loop()
            }
        }

        fun terminate() {
            running = false
        }
    }
}