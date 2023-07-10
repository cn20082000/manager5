package library.cnnavigation

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ClassInfoList
import io.github.classgraph.MethodInfo
import library.cninjector.CnInjector
import library.cnnavigation.annotation.*
import library.cnnavigation.model.CnMethod
import library.cnnavigation.model.CnRequest
import library.cnnavigation.model.CnResponse

object CnNavigation {
    private val controllers: ClassInfoList by lazy {
        ClassGraph().enableAllInfo().scan().getClassesWithAnnotation(Controller::class.java)
    }

    fun process(request: CnRequest): CnResponse {
        for (controller in controllers) {
            val prefix = controller.getAnnotationInfo(Controller::class.java).parameterValues[0].value as String
            if (request.endpoint?.startsWith(prefix) == true) {
                val endpoint = request.endpoint?.replaceFirst(prefix, "") ?: ""

                val method = findMethod(request, endpoint, controller)
                if (method != null) {
                    val result =
                        method.loadClassAndGetMethod().invoke(CnInjector.inject(controller.loadClass())) as CnResponse
                    return CnResponse(
                        request.id,
                        request.method,
                        request.endpoint,
                        request.params,
                        request.header,
                        result.data,
                        result.code,
                    )
                }
            }
        }

        return CnResponse(
            request.id,
            request.method,
            request.endpoint,
            request.params,
            request.header,
            null,
            404,
        )
    }

    private fun findMethod(
        request: CnRequest,
        endpoint: String,
        controller: ClassInfo,
    ): MethodInfo? {
        return when (request.method) {
            CnMethod.GET -> findMethod(endpoint, controller, Get::class.java)
            CnMethod.POST -> findMethod(endpoint, controller, Post::class.java)
            CnMethod.PUT -> findMethod(endpoint, controller, Put::class.java)
            CnMethod.DELETE -> findMethod(endpoint, controller, Delete::class.java)
            else -> null
        }
    }

    private fun findMethod(
        endpoint: String,
        controller: ClassInfo,
        annotation: Class<out Annotation?>
    ): MethodInfo? {
        for (method in controller.methodInfo.filter {
            it.getAnnotationInfo(annotation) != null
        }) {
            val annotationInfo = method.getAnnotationInfo(annotation)

            val annotationEndpoint = annotationInfo.parameterValues[0].value as String
            if (annotationEndpoint == endpoint) {
                return method
            }
        }
        return null
    }
}