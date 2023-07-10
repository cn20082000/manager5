package library.cnnavigation.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Put(
    val name: String = "",
)
