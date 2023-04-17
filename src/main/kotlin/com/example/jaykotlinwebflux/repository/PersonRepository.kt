package com.example.jaykotlinwebflux.repository

import com.example.jaykotlinwebflux.entity.Person
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository: CrudRepository<Person, Long> {
    fun save(person: Person): Person
    fun findByName(name: String): Person
}