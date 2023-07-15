package controller

import library.cnnavigation.annotation.*
import library.cnnavigation.model.CnRequest
import library.cnnavigation.model.CnResponse

@Controller("/welcome")
class WelcomeController {
    @Get
    fun welcome(request: CnRequest, @Body auth: CnResponse?): CnResponse {
        return CnResponse.ok(auth)
    }

    @Post
    fun welcomePost(@Param("auth") auth: String?): String? {
        return null
    }

    @Put
    fun welcomePut(): String {
        return "hello put"
    }
}