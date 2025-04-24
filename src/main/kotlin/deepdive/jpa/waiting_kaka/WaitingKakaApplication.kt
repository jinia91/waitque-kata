package deepdive.jpa.waiting_kaka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class WaitingKakaApplication

fun main(args: Array<String>) {
    runApplication<WaitingKakaApplication>(*args)
}
