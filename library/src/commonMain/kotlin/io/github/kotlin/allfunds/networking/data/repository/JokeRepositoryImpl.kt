package io.github.kotlin.allfunds.networking.data.repository

import io.github.kotlin.allfunds.networking.data.remote.ChuckNorrisApi
import io.github.kotlin.allfunds.networking.domain.model.Joke
import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository

/**
 * Implementation of the JokeRepository
 */
class JokeRepositoryImpl(
    private val api: ChuckNorrisApi
) : JokeRepository {
    /**
     * Get a random joke
     * @return A random joke
     */
    override suspend fun getRandomJoke(): Result<Joke> {
        return try {
            val jokeDto = api.getRandomJoke()
            Result.success(jokeDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     * @return A random joke from the specified category
     */
    override suspend fun getRandomJokeByCategory(category: String): Result<Joke> {
        return try {
            val jokeDto = api.getRandomJokeByCategory(category)
            Result.success(jokeDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get all available categories
     * @return List of available categories
     */
    override suspend fun getCategories(): Result<List<String>> {
        return try {
            val categories = api.getCategories()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Search for jokes
     * @param query The search query
     * @return List of jokes matching the query
     */
    override suspend fun searchJokes(query: String): Result<List<Joke>> {
        return try {
            val searchResponse = api.searchJokes(query)
            Result.success(searchResponse.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}