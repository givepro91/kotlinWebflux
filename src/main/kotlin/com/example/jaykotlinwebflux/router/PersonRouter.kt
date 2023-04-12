package com.example.jaykotlinwebflux.router

import com.example.jaykotlinwebflux.handler.PersonHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class PersonRouter {

    @Bean
    fun personHandlerRouter(personHandler: PersonHandler) = router {
        "/persons".nest {
            GET("/", personHandler::findAll)
            POST("/", personHandler::save)
            GET("/{id}", personHandler::findById)
        }
    }

}