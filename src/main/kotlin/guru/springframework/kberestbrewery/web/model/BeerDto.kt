package guru.springframework.kberestbrewery.web.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class BeerDto(
    @Null
    var id: UUID?,

    @field:NotBlank
    var beerName: String = "",

    @field:NotBlank
    var beerStyle: String = "",

    var upc: String = "",
    var quantityOnHand: Int = 0,
    var price: BigDecimal = BigDecimal(0),
    val createdDate: LocalDateTime = LocalDateTime.now(),
    var lastUpdatedDate: LocalDateTime = LocalDateTime.now(),
)