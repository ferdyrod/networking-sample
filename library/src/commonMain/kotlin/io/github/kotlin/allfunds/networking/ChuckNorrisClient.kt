package io.github.kotlin.allfunds.networking

import io.github.kotlin.allfunds.networking.di.KoinInitializer
import io.github.kotlin.allfunds.networking.domain.model.Joke
import io.github.kotlin.allfunds.networking.domain.usecase.GetCategoriesUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.GetRandomJokeByCategoryUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.GetRandomJokeUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.SearchJokesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.Throws

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
     * @throws IllegalStateException if Koin initialization fails
     */
    @Throws(IllegalStateException::class)
    constructor() {
        try {
            // Only initialize if not already started
            if (!KoinInitializer.isInitialized()) {
                KoinInitializer.init()
            }
        } catch (e: Exception) {
            // Koin might already be started in tests, ignore the exception
            // If it's a different exception, rethrow as IllegalStateException
            if (e !is IllegalStateException) {
                throw IllegalStateException("Failed to initialize Koin", e)
            }
        }
    }
    
    /**
     * Get a random joke
     * @return Result containing a random joke or an exception
     */
    open suspend fun getRandomJoke(): Result<Joke> {
        return getRandomJokeUseCase()
    }
    
    /**
     * Get a random joke from a specific category
     * @param category The category to get a joke from
     * @return Result containing a random joke from the specified category or an exception
     */
    open suspend fun getRandomJokeByCategory(category: String): Result<Joke> {
        return getRandomJokeByCategoryUseCase(category)
    }
    
    /**
     * Get all available categories
     * @return Result containing a list of categories or an exception
     */
    open suspend fun getCategories(): Result<List<String>> {
        return getCategoriesUseCase()
    }
    
    /**
     * Search for jokes
     * @param query The search query (must be at least 3 characters)
     * @return Result containing a list of jokes matching the query or an exception
     */
    open suspend fun searchJokes(query: String): Result<List<Joke>> {
        return searchJokesUseCase(query)
    }
}