package guru.springframework.kberestbrewery.web.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.io.Serializable

class BeerPagedList : PageImpl<BeerDto>, Serializable {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    constructor(
        @JsonProperty("content") content: List<BeerDto>,
        @JsonProperty("number") number: Int,
        @JsonProperty("size") size: Int,
        @JsonProperty("totalElements") totalElements: Long,
        @JsonProperty("pageable") pageable: JsonNode,
        @JsonProperty("last") last: Boolean,
        @JsonProperty("totalPages") totalPages: Int,
        @JsonProperty("sort") sort: JsonNode,
        @JsonProperty("first") first: Boolean,
        @JsonProperty("numberOfElements") numberOfElements: Int
    ) : super(content, PageRequest.of(number, size), totalElements)

    constructor(content: List<BeerDto>, pageable: Pageable, total: Long) : super(content, pageable, total)
    constructor(content: List<BeerDto>) : super(content)

    companion object {
        const val serialVersionUID = 1114715135625836949L
    }
}