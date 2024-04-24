package com.example.jaykotlinwebflux.entity

import com.example.jaykotlinwebflux.dto.PersonInfoDto
import com.example.jaykotlinwebflux.utils.kstZone
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    var name: String = ""

    var email: String = ""

    var password: String = ""

    @CreationTimestamp
    var createdAt: LocalDateTime = LocalDateTime.now(kstZone)
}

fun convertPersonToPersonInfo(person: Person): PersonInfoDto {
    return PersonInfoDto(
        person.id,
        person.name,
        person.email,
        person.createdAt
    )
}