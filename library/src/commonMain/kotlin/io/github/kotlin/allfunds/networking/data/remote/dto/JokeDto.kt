package io.github.kotlin.allfunds.networking.data.remote.dto

import io.github.kotlin.allfunds.networking.domain.model.Joke
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for Chuck Norris joke API response
 */
@Serializable
data class JokeDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("value")
    val value: String,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("categories")
    val categories: List<String> = emptyList(),
    
    @SerialName("created_at")
    val createdAt: String? = null,
    
    @SerialName("updated_at")
    val updatedAt: String? = null,
    
    @SerialName("icon_url")
    val iconUrl: String? = null
) {
    /**
     * Maps DTO to domain model
     */
    fun toDomain(): Joke {
        return Joke(
            id = id,
            value = value,
            url = url,
            categories = categories
        )
    }
}