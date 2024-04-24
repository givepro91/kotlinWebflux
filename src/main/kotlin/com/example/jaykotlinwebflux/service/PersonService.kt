package com.example.jaykotlinwebflux.service

import com.example.jaykotlinwebflux.dto.PersonDto
import com.example.jaykotlinwebflux.dto.PersonInfoDto
import com.example.jaykotlinwebflux.entity.Person
import com.example.jaykotlinwebflux.request.AuthRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PersonService {
    fun getAllPersons(): Flux<PersonInfoDto>
    fun personSave(personDto: PersonDto): Mono<Person>
    fun personAuthenticate(authRequest: AuthRequest): Mono<String>
}