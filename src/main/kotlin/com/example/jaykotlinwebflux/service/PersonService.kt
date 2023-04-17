package com.example.jaykotlinwebflux.service

import com.example.jaykotlinwebflux.dto.PersonDto
import com.example.jaykotlinwebflux.entity.Person
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PersonService {
    fun getAllPersons(): Flux<Person>
    fun personSave(personDto: PersonDto): Mono<Person>
}