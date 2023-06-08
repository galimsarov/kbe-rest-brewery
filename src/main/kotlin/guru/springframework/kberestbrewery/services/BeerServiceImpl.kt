package guru.springframework.kberestbrewery.services

import guru.springframework.kberestbrewery.domain.Beer
import guru.springframework.kberestbrewery.repositories.BeerRepository
import guru.springframework.kberestbrewery.web.mappers.toBeer
import guru.springframework.kberestbrewery.web.mappers.toBeerDtoWithInventory
import guru.springframework.kberestbrewery.web.mappers.toBeerDtoWithZeroInventory
import guru.springframework.kberestbrewery.web.model.BeerDto
import guru.springframework.kberestbrewery.web.model.BeerPagedList
import guru.springframework.kberestbrewery.web.model.BeerStyleEnum
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
@Suppress("unused")
class BeerServiceImpl(private val beerRepository: BeerRepository) : BeerService {
    @Cacheable(cacheNames = ["beerListCache"], condition = "#showInventoryOnHand == false ")
    override fun listBeers(
        beerName: String,
        beerStyle: String,
        pageRequest: PageRequest,
        showInventoryOnHand: Boolean
    ): BeerPagedList {
        val beerPagedList: BeerPagedList
        val beerPage: Page<Beer> = when {
            beerName.isNotBlank() && beerStyle.isNotBlank() ->
                beerRepository.findAllByBeerNameAndBeerStyle(beerName, BeerStyleEnum.valueOf(beerStyle), pageRequest)

            beerName.isNotBlank() && beerStyle.isBlank() ->
                beerRepository.findAllByBeerName(beerName, pageRequest)

            beerName.isBlank() && beerStyle.isNotBlank() ->
                beerRepository.findAllByBeerStyle(BeerStyleEnum.valueOf(beerStyle), pageRequest)

            else -> beerRepository.findAll(pageRequest)
        }
        beerPagedList = if (showInventoryOnHand) {
            BeerPagedList(
                beerPage.content.map { it.toBeerDtoWithInventory() },
                PageRequest.of(beerPage.pageable.pageNumber, beerPage.pageable.pageSize),
                beerPage.totalElements
            )
        } else {
            BeerPagedList(
                beerPage.content.map { it.toBeerDtoWithZeroInventory() },
                PageRequest.of(beerPage.pageable.pageNumber, beerPage.pageable.pageSize),
                beerPage.totalElements
            )
        }
        return beerPagedList
    }

    @Cacheable(cacheNames = ["beerCache"], key = "#beerId", condition = "#showInventoryOnHand == false ")
    override fun getById(beerId: UUID, showInventoryOnHand: Boolean): BeerDto {
        return if (showInventoryOnHand)
            beerRepository.findById(beerId).map { it.toBeerDtoWithInventory() }.orElseThrow { NotFoundException() }
        else
            beerRepository.findById(beerId).map { it.toBeerDtoWithZeroInventory() }.orElseThrow { NotFoundException() }
    }

    override fun saveNewBeer(beerDto: BeerDto): BeerDto {
        return beerRepository.save(beerDto.toBeer()).toBeerDtoWithInventory()
    }

    override fun updateBeer(beerId: UUID, beerDto: BeerDto): BeerDto {
        val beer = beerRepository.findById(beerId).orElseThrow { NotFoundException() }
        beer.beerName = beerDto.beerName
        beer.beerStyle = BeerStyleEnum.valueOf(beerDto.beerStyle.takeIf { it.isNotBlank() } ?: "NONE")
        beer.price = beerDto.price
        beer.upc = beerDto.upc
        return beerRepository.save(beer).toBeerDtoWithInventory()
    }

    @Cacheable(cacheNames = ["beerUpcCache"])
    override fun getByUpc(upc: String): BeerDto {
        return beerRepository.findByUpc(upc).toBeerDtoWithInventory()
    }

    override fun deleteBeerById(beerId: UUID) {
        beerRepository.deleteById(beerId)
    }
}