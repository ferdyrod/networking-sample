package io.github.kotlin.allfunds.networking

import io.github.kotlin.allfunds.networking.di.KoinInitializer
import io.github.kotlin.allfunds.networking.domain.model.Joke
import io.github.kotlin.allfunds.networking.domain.usecase.GetCategoriesUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.GetRandomJokeByCategoryUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.GetRandomJokeUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.SearchJokesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Main client class for the Chuck Norris API
 * 
 * This class provides a simple interface to access Chuck Norris jokes
 * using clean architecture principles with Koin dependency injection.
 */
open class ChuckNorrisClient : KoinComponent {
    
    // Inject use cases from Koin
    private val getRandomJokeUseCase: GetRandomJokeUseCase by inject()
    private val getRandomJokeByCategoryUseCase: GetRandomJokeByCategoryUseCase by inject()
    private val getCategoriesUseCase: GetCategoriesUseCase by inject()
    private val searchJokesUseCase: SearchJokesUseCase by inject()
    
    /**
     * Default constructor that initializes Koin if needed
     */
    constructor() {
        try {
            // Only initialize if not already started
            if (!KoinInitializer.isInitialized()) {
                KoinInitializer.init()
            }
        } catch (e: Exception) {
            // Koin might already be started in tests, ignore the exception
        }
    }
    
    /**
     * Get a random joke
     * @return A random joke
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    open suspend fun getRandomJoke(): Joke {
        return try {
            getRandomJokeUseCase().getOrThrow()
        } catch (e: Throwable) {
            throw Exception("Failed to get random joke: ${e.message}", e)
        }
    }

    /**
     * Get a random joke
     * @return A random joke
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    open suspend fun getRandomJokeResult(): Result<Joke> {
        return getRandomJokeUseCase()
    }


    
    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     * @return A random joke from the specified category
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    open suspend fun getRandomJokeByCategory(category: String): Joke {
        return try {
            getRandomJokeByCategoryUseCase(category).getOrThrow()
        } catch (e: Throwable) {
            throw Exception("Failed to get random joke by category '$category': ${e.message}", e)
        }
    }
    
    /**
     * Get all available categories
     * @return List of available categories
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    open suspend fun getCategories(): List<String> {
        return try {
            getCategoriesUseCase().getOrThrow()
        } catch (e: Throwable) {
            throw Exception("Failed to get categories: ${e.message}", e)
        }
    }
    /**
     * Get all available categories
     * @return List of available categories
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    open suspend fun getCategoriesResult(): Result<List<String>> {
        return getCategoriesUseCase()
    }
    
    /**
     * Search for jokes
     * @param query The search query (must be at least 3 characters)
     * @return List of jokes matching the query
     * @throws Exception if the request fails
     */
    @Throws(Exception::class)
    open suspend fun searchJokes(query: String): List<Joke> {
        return try {
            searchJokesUseCase(query).getOrThrow()
        } catch (e: Throwable) {
            throw Exception("Failed to search jokes with query '$query': ${e.message}", e)
        }
    }
}