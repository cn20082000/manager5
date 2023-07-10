package library.cninjector

object CnInjector {
    private val instances = mutableMapOf<Class<*>, Any>()

    fun <T: Any> inject(clazz: Class<T>): T? {
        try {
            val obj = clazz.cast(instances[clazz])
            if (obj != null) {
                return obj
            }
        } catch (ex: Exception) {
            cnInjectorLog(ex)
        }
        return createInstance(clazz)
    }

    private fun <T: Any> createInstance(clazz: Class<T>): T? {
        return try {
            val obj = clazz.getConstructor().newInstance()
            instances[clazz] = obj
            obj
        } catch (ex: Exception) {
            cnInjectorLog(ex)
            null
        }
    }
}