package io.github.kotlin.allfunds.networking.data.remote

import io.github.kotlin.allfunds.networking.data.remote.dto.JokeDto
import io.github.kotlin.allfunds.networking.data.remote.dto.SearchResponseDto
import kotlin.Throws
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*

/**
 * Interface for the Chuck Norris API
 */
interface ChuckNorrisApi {
    /**
     * Get a random joke
     * @throws ClientRequestException if the request fails
     * @throws IOException if there's an I/O error
     * @throws Exception for any other error
     */
    @Throws(ClientRequestException::class, IOException::class, Exception::class)
    suspend fun getRandomJoke(): JokeDto
    
    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     * @throws ClientRequestException if the request fails
     * @throws IOException if there's an I/O error
     * @throws Exception for any other error
     */
    @Throws(ClientRequestException::class, IOException::class, Exception::class)
    suspend fun getRandomJokeByCategory(category: String): JokeDto
    
    /**
     * Get all available categories
     * @throws ClientRequestException if the request fails
     * @throws IOException if there's an I/O error
     * @throws Exception for any other error
     */
    @Throws(ClientRequestException::class, IOException::class, Exception::class)
    suspend fun getCategories(): List<String>
    
    /**
     * Search for jokes
     * @param query The search query
     * @throws ClientRequestException if the request fails
     * @throws IOException if there's an I/O error
     * @throws Exception for any other error
     */
    @Throws(ClientRequestException::class, IOException::class, Exception::class)
    suspend fun searchJokes(query: String): SearchResponseDto
}