package io.github.kotlin.allfunds.networking.data.remote

import io.github.kotlin.allfunds.networking.data.remote.dto.JokeDto
import io.github.kotlin.allfunds.networking.data.remote.dto.SearchResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Implementation of the Chuck Norris API
 */
class ChuckNorrisApiImpl : ChuckNorrisApi {
    private val baseUrl = "https://api.chucknorris.io/jokes"
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                isLenient = true
            })
        }
    }
    
    /**
     * Get a random joke
     */
    override suspend fun getRandomJoke(): JokeDto {
        return client.get("$baseUrl/random").body()
    }
    
    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     */
    override suspend fun getRandomJokeByCategory(category: String): JokeDto {
        return client.get("$baseUrl/random") {
            parameter("category", category)
        }.body()
    }
    
    /**
     * Get all available categories
     */
    override suspend fun getCategories(): List<String> {
        return client.get("$baseUrl/categories").body()
    }
    
    /**
     * Search for jokes
     * @param query The search query
     */
    override suspend fun searchJokes(query: String): SearchResponseDto {
        return client.get("$baseUrl/search") {
            parameter("query", query)
        }.body()
    }
}