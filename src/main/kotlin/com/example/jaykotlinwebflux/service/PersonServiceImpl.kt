package com.example.jaykotlinwebflux.service

import com.example.jaykotlinwebflux.dto.PersonDto
import com.example.jaykotlinwebflux.dto.PersonInfoDto
import com.example.jaykotlinwebflux.entity.Person
import com.example.jaykotlinwebflux.entity.convertPersonToPersonInfo
import com.example.jaykotlinwebflux.exception.AlreadyExistEmailException
import com.example.jaykotlinwebflux.repository.PersonRepository
import com.example.jaykotlinwebflux.request.AuthRequest
import com.example.jaykotlinwebflux.security.JwtUtil
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.scheduler.Schedulers
import java.time.LocalDateTime.now
import java.util.*
import javax.transaction.Transactional

@Service
class PersonServiceImpl(
    private val personRepository: PersonRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) : PersonService {

    override fun getAllPersons(): Flux<PersonInfoDto> {
        return Flux.fromIterable(
            personRepository.findAll().map { convertPersonToPersonInfo(it) }
        )
    }

    /**
     * Spring Boot WebFlux와 JPA를 함께 사용할 수 있지만,
     * JPA가 blocking I/O를 사용하기 때문에 적절한 방식으로 non-blocking I/O와 결합해야 합니다
     */
    @Transactional
    override fun personSave(personDto: PersonDto): Mono<Person> {

        // Mono.fromCallable() 메서드를 사용하여 blocking I/O를 호출
        return availableEmailCheck(personDto.email)
            .subscribeOn(Schedulers.boundedElastic()) // subscribeOn() 메서드를 사용하여 elastic 스레드 풀에서 실행하도록 설정
            .flatMap {
                if (it) {
                    Mono.just(
                        personRepository.save(
                            Person().apply {
                                this.name = personDto.name
                                this.email = personDto.email
                                this.password = passwordEncoder.encode(personDto.password)
                                this.createdAt = now()
                            }
                        )
                    )
                } else {
                    Mono.error(AlreadyExistEmailException())
                }
            }
            .onErrorResume { throwable ->
                if (throwable is NoSuchElementException) {
                    Mono.empty()
                } else {
                    Mono.error(throwable)
                }
            }
    }

    override fun personAuthenticate(authRequest: AuthRequest): Mono<String> {
        return Mono.fromCallable { personRepository.findByEmail(authRequest.email) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { person ->
                if (person != null) {
                    if (BCryptPasswordEncoder().matches(authRequest.password, person.password)) {
                        val jwt = jwtUtil.generateToken(person.name)
                        Mono.just(jwt)
                    } else {
                        Mono.error(AlreadyExistEmailException())
                    }
                } else {
                    Mono.error(AlreadyExistEmailException())
                }
            }
    }

    private fun availableEmailCheck(email: String): Mono<Boolean> {
        return Mono.fromCallable { personRepository.findByEmail(email) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap {
                Mono.just(false)
            }.switchIfEmpty(Mono.just(true))
    }
}