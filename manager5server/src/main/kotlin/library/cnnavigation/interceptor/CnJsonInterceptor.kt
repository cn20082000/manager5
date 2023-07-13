package library.cnnavigation.interceptor

import com.google.gson.GsonBuilder
import library.cnnavigation.cnNavigationLog
import library.cnnavigation.model.CnRequest
import library.cnnavigation.model.CnResponse
import library.cnserver.CnInterceptor
import java.time.LocalDateTime

class CnJsonInterceptor : CnInterceptor {
    override fun intercept(message: Any?, next: List<CnInterceptor>): Any? {
        val start = System.currentTimeMillis()
        val startTime = LocalDateTime.now()
        val gson = GsonBuilder().create()
        val request = try {
            gson.fromJson(message as String, CnRequest::class.java)
        } catch (ex: Exception) {
            cnNavigationLog(ex)
            null
        }

        if (request != null) {
            try {
                request.validate()

                if (next.isNotEmpty()) {
                    val response = next.first().intercept(request, next.subList(1, next.size))
                    if (response is CnResponse) {
                        response.headers?.put("requestTime", startTime.toString())
                        response.headers?.put("processIn", System.currentTimeMillis() - start)
                        response.validate()
                        return gson.toJson(response)
                    }
                }
            } catch (ex: Exception) {
                cnNavigationLog(ex)
            }
            return gson.toJson(
                CnResponse(
                    request.id,
                    request.method,
                    request.endpoint,
                    mutableMapOf(
                        Pair("requestTime", startTime.toString()),
                        Pair("processIn", System.currentTimeMillis() - start),
                    ),
                    request.params,
                    null,
                    404
                )
            )
        }
        return null
    }
}