package com.example.jaykotlinwebflux.entity

import com.example.jaykotlinwebflux.utils.utcZone
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

    @CreationTimestamp
    var createdAt: LocalDateTime = LocalDateTime.now(utcZone)
}