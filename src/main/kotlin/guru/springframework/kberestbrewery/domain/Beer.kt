package guru.springframework.kberestbrewery.domain

import guru.springframework.kberestbrewery.web.model.BeerStyleEnum
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
data class Beer(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    var id: UUID? = UUID.randomUUID(),

    @Version
    var version: Int = 0,

    var beerName: String = "",
    var beerStyle: BeerStyleEnum = BeerStyleEnum.NONE,
    var upc: String = "",

    var quantityOnHand: Int = 0,
    var price: BigDecimal = BigDecimal(0),

    var createdDate: LocalDateTime = LocalDateTime.now(),
    var lastModifiedDate: LocalDateTime = LocalDateTime.now(),
)
