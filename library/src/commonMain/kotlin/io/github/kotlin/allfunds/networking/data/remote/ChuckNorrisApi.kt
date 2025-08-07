package io.github.kotlin.allfunds.networking.data.remote

import io.github.kotlin.allfunds.networking.data.remote.dto.JokeDto
import io.github.kotlin.allfunds.networking.data.remote.dto.SearchResponseDto

/**
 * Interface for the Chuck Norris API
 */
interface ChuckNorrisApi {
    /**
     * Get a random joke
     */
    suspend fun getRandomJoke(): JokeDto
    
    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     */
    suspend fun getRandomJokeByCategory(category: String): JokeDto
    
    /**
     * Get all available categories
     */
    suspend fun getCategories(): List<String>
    
    /**
     * Search for jokes
     * @param query The search query
     */
    suspend fun searchJokes(query: String): SearchResponseDto
}