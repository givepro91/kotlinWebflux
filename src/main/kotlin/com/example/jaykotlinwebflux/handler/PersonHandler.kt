package com.example.jaykotlinwebflux.handler

import com.example.jaykotlinwebflux.entity.Person
import com.example.jaykotlinwebflux.repository.PersonRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class PersonHandler(
    private val personRepository: PersonRepository
) {

    fun findAll(request: ServerRequest): Mono<ServerResponse> {
        val persons = personRepository.findAll()
        return ServerResponse.ok().body(persons, Person::class.java)
    }

}