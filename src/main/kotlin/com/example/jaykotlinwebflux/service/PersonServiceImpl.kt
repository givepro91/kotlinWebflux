package com.example.jaykotlinwebflux.service

import com.example.jaykotlinwebflux.dto.PersonDto
import com.example.jaykotlinwebflux.entity.Person
import com.example.jaykotlinwebflux.repository.PersonRepository
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
    private val personRepository: PersonRepository
)
    : PersonService {
    override fun getAllPersons(): Flux<Person> {
        return Flux.fromIterable(personRepository.findAll())
    }

    /**
     * Spring Boot WebFlux와 JPA를 함께 사용할 수 있지만,
     * JPA가 blocking I/O를 사용하기 때문에 적절한 방식으로 non-blocking I/O와 결합해야 합니다
     */
    @Transactional
    override fun personSave(personDto: PersonDto): Mono<Person> {

        // Mono.fromCallable() 메서드를 사용하여 blocking I/O를 호출
        return Mono.fromCallable { personRepository.findByName(personDto.name) } // error
            .subscribeOn(Schedulers.boundedElastic()) // subscribeOn() 메서드를 사용하여 elastic 스레드 풀에서 실행하도록 설정
            .flatMap {
                Mono.just(
                    personRepository.save(
                        Person().apply {
                            this.name = personDto.name
                            this.createdAt = now()
                        }
                    )
                )
            }
            .onErrorResume { throwable ->
                if (throwable is NoSuchElementException) {
                    Mono.empty()
                } else {
                    Mono.error(throwable)
                }
            }
    }
}