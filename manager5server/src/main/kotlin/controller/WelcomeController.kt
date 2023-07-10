package controller

import library.cnnavigation.annotation.Controller
import library.cnnavigation.annotation.Get
import library.cnnavigation.annotation.Post
import library.cnnavigation.annotation.Put
import library.cnnavigation.model.CnResponse

@Controller("/welcome")
class WelcomeController {

    @Get
    fun welcome(): CnResponse {
        return CnResponse.ok("hello")
    }

    @Post
    fun welcomePost(): CnResponse {
        return CnResponse.ok("hello push")
    }

    @Put
    fun welcomePut(): CnResponse {
        return CnResponse.ok("hello put")
    }
}