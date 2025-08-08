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
 *
 */
class ChuckNorrisApiImpl() : ChuckNorrisApi {
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
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    override suspend fun getRandomJoke(): JokeDto {
        return try {
            client.get("$baseUrl/random").body()
        } catch (e: Throwable) {
            throw Exception("Failed to get random joke: ${e.message}", e)
        }
    }

    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    override suspend fun getRandomJokeByCategory(category: String): JokeDto {
        return try {
            client.get("$baseUrl/random") {
                parameter("category", category)
            }.body()
        } catch (e: Throwable) {
            throw Exception("Failed to get random joke by category '$category': ${e.message}", e)
        }
    }

    /**
     * Get all available categories
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    override suspend fun getCategories(): List<String> {
        return try {
            client.get("$baseUrl/categories").body()
        } catch (e: Throwable) {
            throw Exception("Failed to get categories: ${e.message}", e)
        }
    }

    /**
     * Search for jokes
     * @param query The search query
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    override suspend fun searchJokes(query: String): SearchResponseDto {
        return try {
            client.get("$baseUrl/search") {
                parameter("query", query)
            }.body()
        } catch (e: Throwable) {
            throw Exception("Failed to search jokes with query '$query': ${e.message}", e)
        }
    }
}