package io.github.kotlin.allfunds.networking.data.remote.dto

import io.github.kotlin.allfunds.networking.domain.model.Joke
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for Chuck Norris joke search API response
 */
@Serializable
data class SearchResponseDto(
    @SerialName("total")
    val total: Int,
    
    @SerialName("result")
    val result: List<JokeDto>
) {
    /**
     * Maps DTO to domain model list
     */
    fun toDomain(): List<Joke> {
        return result.map { it.toDomain() }
    }
}