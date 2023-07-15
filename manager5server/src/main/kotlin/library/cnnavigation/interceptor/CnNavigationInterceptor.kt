package library.cnnavigation.interceptor

import library.cnnavigation.CnNavigation
import library.cnnavigation.cnNavigationLog
import library.cnnavigation.model.CnRequest
import library.cnserver.CnInterceptor

class CnNavigationInterceptor : CnInterceptor {
    override fun intercept(message: Any?, next: List<CnInterceptor>): Any? {
        try {
            if (message is CnRequest) {
                message.validate()
                return CnNavigation.process(message)
            }
        } catch (ex: Exception) {
            cnNavigationLog(ex)
        }
        return null
    }
}