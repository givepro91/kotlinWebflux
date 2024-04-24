package com.example.jaykotlinwebflux.dto

import java.time.LocalDateTime

data class PersonInfoDto (
    val id: Long,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime
)