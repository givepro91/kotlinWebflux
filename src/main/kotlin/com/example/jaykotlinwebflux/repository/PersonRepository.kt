package com.example.jaykotlinwebflux.repository

import com.example.jaykotlinwebflux.entity.Person
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository: ReactiveMongoRepository<Person, Long> {
}