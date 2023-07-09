package library.cnnavigation

import com.google.gson.GsonBuilder
import library.cnnavigation.model.CnRequest
import library.cnserver.CnInterceptor

class CnNavigationInterceptor : CnInterceptor {

    override fun intercept(message: Any?, next: List<CnInterceptor>): Any? {
        try {
            val gson = GsonBuilder().create()

            val request = gson.fromJson(message as String, CnRequest::class.java)
            request.validate()
            val response = CnNavigation.process(request)
            response.validate()

            return gson.toJson(response)
        } catch (ex: Exception) {
            cnNavigationLog(ex)
        }
        return null
    }
}