package com.example.jaykotlinwebflux.handler

import com.example.jaykotlinwebflux.dto.PersonDto
import com.example.jaykotlinwebflux.entity.Person
import com.example.jaykotlinwebflux.service.PersonService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class PersonHandler(
    private val personService: PersonService
) {

    fun findAll(request: ServerRequest): Mono<ServerResponse> {
        val persons = personService.getAllPersons()
        return ServerResponse.ok().body(persons, Person::class.java)
    }

    fun save(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(PersonDto::class.java)
            .flatMap { person ->
                personService.personSave(person)
            }.flatMap {
                ServerResponse.ok().bodyValue(it)
            }
    }

}