package io.github.kotlin.allfunds.networking.domain.repository

import io.github.kotlin.allfunds.networking.domain.model.Joke

/**
 * Repository interface for accessing Chuck Norris jokes
 */
interface JokeRepository {
    /**
     * Get a random joke
     * @return A random joke
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    suspend fun getRandomJoke(): Result<Joke>
    
    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     * @return A random joke from the specified category
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    suspend fun getRandomJokeByCategory(category: String): Result<Joke>
    
    /**
     * Get all available categories
     * @return List of available categories
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    suspend fun getCategories(): Result<List<String>>
    
    /**
     * Search for jokes
     * @param query The search query
     * @return List of jokes matching the query
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    suspend fun searchJokes(query: String): Result<List<Joke>>
}