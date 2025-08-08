package io.github.kotlin.allfunds.networking.data.remote

import io.github.kotlin.allfunds.networking.data.remote.dto.JokeDto
import io.github.kotlin.allfunds.networking.data.remote.dto.SearchResponseDto

/**
 * Interface for the Chuck Norris API
 */
interface ChuckNorrisApi {
    /**
     * Get a random joke
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    suspend fun getRandomJoke(): JokeDto
    
    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    suspend fun getRandomJokeByCategory(category: String): JokeDto
    
    /**
     * Get all available categories
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    suspend fun getCategories(): List<String>
    
    /**
     * Search for jokes
     * @param query The search query
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    suspend fun searchJokes(query: String): SearchResponseDto
}