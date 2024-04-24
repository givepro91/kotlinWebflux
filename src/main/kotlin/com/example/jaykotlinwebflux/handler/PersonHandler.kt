package com.example.jaykotlinwebflux.handler

import com.example.jaykotlinwebflux.dto.PersonDto
import com.example.jaykotlinwebflux.dto.PersonInfoDto
import com.example.jaykotlinwebflux.exception.AlreadyExistEmailException
import com.example.jaykotlinwebflux.request.AuthRequest
import com.example.jaykotlinwebflux.security.JwtUtil
import com.example.jaykotlinwebflux.service.PersonService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class PersonHandler(
    private val personService: PersonService,
) {

    fun findAll(request: ServerRequest): Mono<ServerResponse> {
        val persons = personService.getAllPersons()
        return ServerResponse.ok().body(persons, PersonInfoDto::class.java)
    }

    fun save(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(PersonDto::class.java)
            .flatMap { person ->
                personService.personSave(person)
            }.flatMap {
                ServerResponse.ok().bodyValue(it)
            }.onErrorResume(AlreadyExistEmailException::class.java) {
                ServerResponse.badRequest().body(BodyInserters.fromValue("email is exist"))
            }
    }

    fun createAuthenticationToken(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(AuthRequest::class.java)
            .flatMap { personService.personAuthenticate(it) }
            .flatMap { ServerResponse.ok().body(BodyInserters.fromValue(mapOf("token" to it))) }
            .onErrorResume(AlreadyExistEmailException::class.java) {
                ServerResponse.badRequest().body(BodyInserters.fromValue("authenticate false"))
            }
    }

}