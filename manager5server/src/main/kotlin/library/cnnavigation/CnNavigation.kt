package library.cnnavigation

import com.google.gson.GsonBuilder
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ClassInfoList
import io.github.classgraph.MethodInfo
import library.cninjector.CnInjector
import library.cnnavigation.annotation.*
import library.cnnavigation.model.CnMethod
import library.cnnavigation.model.CnRequest
import library.cnnavigation.model.CnResponse
import kotlin.math.min

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
                    val parameters = processParameters(method, request)
                    val result = method.loadClassAndGetMethod().invoke(CnInjector.inject(controller.loadClass()), *parameters.toTypedArray())

                    if (result is CnResponse) {
                        return CnResponse(
                            request.id,
                            request.method,
                            request.endpoint,
                            mutableMapOf(),
                            request.params,
                            result.body,
                            result.code,
                        )
                    } else {
                        return CnResponse(
                            request.id,
                            request.method,
                            request.endpoint,
                            mutableMapOf(),
                            request.params,
                            result,
                            if (result != null) 200 else 404,
                        )
                    }
                }
            }
        }

        return CnResponse(
            request.id,
            request.method,
            request.endpoint,
            mutableMapOf(),
            request.params,
            null,
            404,
        )
    }

    private fun processParameters(method: MethodInfo, request: CnRequest): List<Any?> {
        val result = mutableListOf<Any?>()

        val parameterInfo = method.parameterInfo
        val parameters = method.loadClassAndGetMethod().parameters
        for (index in 0 until min(parameterInfo.size, parameters.size)) {
            if (parameters[index].type == CnRequest::class.java) {
                result.add(request)
                continue
            }

            val headerAnno = parameterInfo[index].getAnnotationInfo(Header::class.java)
            if (headerAnno != null) {
                val headerValue = headerAnno.parameterValues[0].value as String
                if (request.headers?.get(headerValue) is String && parameters[index].type == java.lang.String::class.java) {
                    result.add(request.headers?.get(headerValue))
                    continue
                }
                if (request.headers?.get(headerValue) is Double) {
                    if (parameters[index].type == java.lang.Byte::class.java) {
                        result.add((request.headers?.get(headerValue) as Double).toLong().toByte())
                        continue
                    }
                    if (parameters[index].type == java.lang.Short::class.java) {
                        result.add((request.headers?.get(headerValue) as Double).toLong().toShort())
                        continue
                    }
                    if (parameters[index].type == java.lang.Integer::class.java) {
                        result.add((request.headers?.get(headerValue) as Double).toInt())
                        continue
                    }
                    if (parameters[index].type == java.lang.Long::class.java) {
                        result.add((request.headers?.get(headerValue) as Double).toLong())
                        continue
                    }
                    if (parameters[index].type == java.lang.Float::class.java) {
                        result.add((request.headers?.get(headerValue) as Double).toFloat())
                        continue
                    }
                    if (parameters[index].type == java.lang.Double::class.java) {
                        result.add(request.headers?.get(headerValue) as Double)
                        continue
                    }
                }
            }

            val paramAnno = parameterInfo[index].getAnnotationInfo(Param::class.java)
            if (paramAnno != null) {
                val paramValue = paramAnno.parameterValues[0].value as String
                if (request.params?.get(paramValue) is String && parameters[index].type == java.lang.String::class.java) {
                    result.add(request.params?.get(paramValue))
                    continue
                }
                if (request.params?.get(paramValue) is Double) {
                    if (parameters[index].type == java.lang.Byte::class.java) {
                        result.add((request.params?.get(paramValue) as Double).toLong().toByte())
                        continue
                    }
                    if (parameters[index].type == java.lang.Short::class.java) {
                        result.add((request.params?.get(paramValue) as Double).toLong().toShort())
                        continue
                    }
                    if (parameters[index].type == java.lang.Integer::class.java) {
                        result.add((request.params?.get(paramValue) as Double).toInt())
                        continue
                    }
                    if (parameters[index].type == java.lang.Long::class.java) {
                        result.add((request.params?.get(paramValue) as Double).toLong())
                        continue
                    }
                    if (parameters[index].type == java.lang.Float::class.java) {
                        result.add((request.params?.get(paramValue) as Double).toFloat())
                        continue
                    }
                    if (parameters[index].type == java.lang.Double::class.java) {
                        result.add(request.params?.get(paramValue) as Double)
                        continue
                    }
                }
            }

            val bodyAnno = parameterInfo[index].getAnnotationInfo(Body::class.java)
            if (bodyAnno != null) {
                if (request.body is String && parameters[index].type == java.lang.String::class.java) {
                    result.add(request.body)
                    continue
                }
                if (request.body is Double) {
                    if (parameters[index].type == java.lang.Byte::class.java) {
                        result.add(request.body.toLong().toByte())
                        continue
                    }
                    if (parameters[index].type == java.lang.Short::class.java) {
                        result.add(request.body.toLong().toShort())
                        continue
                    }
                    if (parameters[index].type == java.lang.Integer::class.java) {
                        result.add(request.body.toInt())
                        continue
                    }
                    if (parameters[index].type == java.lang.Long::class.java) {
                        result.add(request.body.toLong())
                        continue
                    }
                    if (parameters[index].type == java.lang.Float::class.java) {
                        result.add(request.body.toFloat())
                        continue
                    }
                    if (parameters[index].type == java.lang.Double::class.java) {
                        result.add(request.body)
                        continue
                    }
                }
                if (request.body != null) {
                    val gson = GsonBuilder().create()
                    val bodyString = gson.toJson(request.body)

                    result.add(gson.fromJson(bodyString, parameters[index].type))
                    continue
                }
            }
            result.add(null)
        }

        return result
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