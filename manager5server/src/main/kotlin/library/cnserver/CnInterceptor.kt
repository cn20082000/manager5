package library.cnserver

interface CnInterceptor {
    fun intercept(message: Any?, next: List<CnInterceptor>): Any?
}