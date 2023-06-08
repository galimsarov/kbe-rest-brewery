package guru.springframework.kberestbrewery.services

import guru.springframework.kberestbrewery.web.model.BeerDto
import guru.springframework.kberestbrewery.web.model.BeerPagedList
import org.springframework.data.domain.PageRequest
import java.util.*

interface BeerService {
    fun listBeers(
        beerName: String,
        beerStyle: String,
        pageRequest: PageRequest,
        showInventoryOnHand: Boolean
    ): BeerPagedList

    fun getById(beerId: UUID, showInventoryOnHand: Boolean): BeerDto

    fun saveNewBeer(beerDto: BeerDto): BeerDto

    fun updateBeer(beerId: UUID, beerDto: BeerDto): BeerDto

    fun getByUpc(upc: String): BeerDto

    fun deleteBeerById(beerId: UUID)
}