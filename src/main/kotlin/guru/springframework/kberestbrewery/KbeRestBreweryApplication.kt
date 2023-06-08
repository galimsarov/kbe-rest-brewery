package guru.springframework.kberestbrewery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KbeRestBreweryApplication

fun main(args: Array<String>) {
    runApplication<KbeRestBreweryApplication>(*args)
}
