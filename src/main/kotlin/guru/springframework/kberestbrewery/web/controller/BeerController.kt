package guru.springframework.kberestbrewery.web.controller

import guru.springframework.kberestbrewery.services.BeerService
import guru.springframework.kberestbrewery.web.model.BeerDto
import guru.springframework.kberestbrewery.web.model.BeerPagedList
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@RequestMapping("/api/v1/")
@RestController
@Suppress("unused")
class BeerController(private val beerService: BeerService) {
    @GetMapping("beer/")
    fun listBeers(
        @RequestParam(
            value = "pageNumber",
            defaultValue = DEFAULT_PAGE_NUMBER.toString(),
            required = false
        ) pageNumber: Int,
        @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE.toString(), required = false) pageSize: Int,
        @RequestParam(value = "beerName", defaultValue = "", required = false) beerName: String,
        @RequestParam(value = "beerStyle", defaultValue = "", required = false) beerStyle: String,
        @RequestParam(value = "showInventoryOnHand", required = false) showInventoryOnHand: Boolean?
    ): ResponseEntity<BeerPagedList> {
        val beerList = beerService.listBeers(
            beerName,
            beerStyle,
            PageRequest.of(
                pageNumber.takeIf { it >= 0 } ?: DEFAULT_PAGE_NUMBER,
                pageSize.takeIf { it > 0 } ?: DEFAULT_PAGE_SIZE,
            ),
            showInventoryOnHand ?: true
        )
        return ResponseEntity(beerList, HttpStatus.OK)
    }

    @GetMapping("beer/{beerId}")
    fun getBeerById(
        @PathVariable("beerId") beerId: UUID,
        @RequestParam(value = "showInventoryOnHand", required = false) showInventoryOnHand: Boolean?
    ): ResponseEntity<BeerDto> {
        return ResponseEntity(beerService.getById(beerId, showInventoryOnHand ?: true), HttpStatus.OK)
    }

    @GetMapping("beerUpc/{upc}")
    fun getBeerByUpc(@PathVariable("upc") upc: String): ResponseEntity<BeerDto> {
        return ResponseEntity(beerService.getByUpc(upc), HttpStatus.OK)
    }

    @PostMapping("beer/")
    fun saveNewBeer(@RequestBody @Validated beerDto: BeerDto): ResponseEntity<*> {
        val (id) = beerService.saveNewBeer(beerDto)
        return ResponseEntity
            .created(
                UriComponentsBuilder
                    .fromHttpUrl("http://api.springframework.guru/api/v1/beer/" + id.toString())
                    .build().toUri()
            )
            .build<Any>()
    }

    @PutMapping("beer/{beerId}")
    fun updateBeerById(
        @PathVariable("beerId") beerId: UUID,
        @RequestBody @Validated beerDto: BeerDto
    ): ResponseEntity<*> {
        return ResponseEntity(beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("beer/{beerId}")
    fun deleteBeerById(@PathVariable("beerId") beerId: UUID): ResponseEntity<Void> {
        beerService.deleteBeerById(beerId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    companion object {
        private const val DEFAULT_PAGE_NUMBER = 0
        private const val DEFAULT_PAGE_SIZE = 25
    }
}