package library.cnnavigation.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Delete(
    val name: String = "",
)
