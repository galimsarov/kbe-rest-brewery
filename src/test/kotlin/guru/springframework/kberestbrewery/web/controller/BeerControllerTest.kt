package guru.springframework.kberestbrewery.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import guru.springframework.kberestbrewery.bootstrap.BeerLoader
import guru.springframework.kberestbrewery.services.BeerService
import guru.springframework.kberestbrewery.web.model.BeerDto
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(BeerController::class)
class BeerControllerTest {
    @MockBean
    private lateinit var beerService: BeerService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var validBeer: BeerDto

    @BeforeEach
    fun setUp() {
        validBeer = BeerDto(
            id = UUID.randomUUID(),
            beerName = "Beer1",
            beerStyle = "PALE_ALE",
            upc = BeerLoader.BEER_2_UPC,
        )
    }

    @Test
    fun getBeer() {
        Mockito
            .`when`(beerService.getById(validBeer.id ?: UUID.randomUUID(), true))
            .thenReturn(validBeer)
        mockMvc.perform(get("/api/v1/beer/" + validBeer.id.toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", `is`(validBeer.id.toString())))
            .andExpect(jsonPath("$.beerName", `is`("Beer1")))
    }

    @Test
    fun handlePost() {
        //given
        val beerDto = validBeer.apply { id = null }
        val savedDto = BeerDto(
            id = UUID.randomUUID(),
            beerName = "New Beer"
        )
        val beerDtoJson = objectMapper.writeValueAsString(beerDto)
        Mockito.`when`(beerService.saveNewBeer(beerDto)).thenReturn(savedDto)
        mockMvc.perform(
            post("/api/v1/beer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson)
        )
            .andExpect(status().isCreated())
    }

    @Test
    fun handleUpdate() {
        //given
        val beerDto = validBeer.apply { id = null }
        val beerId = UUID.randomUUID()
        val beerDtoJson = objectMapper.writeValueAsString(beerDto)
        //when
        Mockito.`when`(beerService.updateBeer(beerId, beerDto)).thenReturn(beerDto)
        mockMvc.perform(
            put("/api/v1/beer/$beerId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson)
        )
            .andExpect(status().isNoContent())

    }
}