package guru.springframework.kberestbrewery.web.mappers

import guru.springframework.kberestbrewery.domain.Beer
import guru.springframework.kberestbrewery.web.model.BeerDto
import guru.springframework.kberestbrewery.web.model.BeerStyleEnum

fun Beer.toBeerDtoWithInventory() = BeerDto(
    id = id,
    beerName = beerName,
    beerStyle = beerStyle.takeIf { it != BeerStyleEnum.NONE }.toString(),
    upc = upc,
    quantityOnHand = quantityOnHand,
    price = price,
    createdDate = createdDate,
    lastUpdatedDate = lastModifiedDate
)

fun Beer.toBeerDtoWithZeroInventory() = BeerDto(
    id = id,
    beerName = beerName,
    beerStyle = beerStyle.takeIf { it != BeerStyleEnum.NONE }.toString(),
    upc = upc,
    quantityOnHand = 0,
    price = price,
    createdDate = createdDate,
    lastUpdatedDate = lastModifiedDate
)

fun BeerDto.toBeer() = Beer(
    id = id,
    beerName = beerName,
    beerStyle = BeerStyleEnum.valueOf(beerStyle.takeIf { it.isNotBlank() } ?: "NONE"),
    upc = upc,
    quantityOnHand = quantityOnHand,
    price = price,
    createdDate = createdDate,
    lastModifiedDate = lastUpdatedDate
)