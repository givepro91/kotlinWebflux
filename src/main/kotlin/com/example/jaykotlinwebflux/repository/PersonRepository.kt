package com.example.jaykotlinwebflux.repository

import com.example.jaykotlinwebflux.entity.Person
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository: CrudRepository<Person, Long> {
}