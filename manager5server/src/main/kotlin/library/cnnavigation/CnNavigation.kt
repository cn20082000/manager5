package library.cnnavigation

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfoList
import library.cnnavigation.annotation.Controller
import library.cnnavigation.model.CnRequest
import library.cnnavigation.model.CnResponse

object CnNavigation {
    private val controllers: ClassInfoList by lazy {
        ClassGraph().enableAllInfo().scan().getClassesWithAnnotation(Controller::class.java)
    }

    fun process(request: CnRequest): CnResponse {
        return CnResponse(
            request.id,
            request.method,
            request.endpoint,
            request.params,
            request.header,
            request.data,
            200,
        )
    }
}