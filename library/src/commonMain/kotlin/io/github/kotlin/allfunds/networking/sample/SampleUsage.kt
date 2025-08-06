package io.github.kotlin.allfunds.networking.sample

import io.github.kotlin.allfunds.networking.ChuckNorrisClient
import io.github.kotlin.allfunds.networking.di.KoinInitializer

/**
 * Sample usage of the ChuckNorrisClient with Koin
 * 
 * This is an example of how to use the library in your application.
 */
object SampleUsage {
    /**
     * Initialize the library
     * 
     * Call this method once at the start of your application
     */
    fun initialize() {
        // Initialize Koin with the network module
        KoinInitializer.init()
    }
    
    /**
     * Example of how to use the ChuckNorrisClient
     * 
     * @param onJokeReceived Callback for when a joke is received
     * @param onError Callback for when an error occurs
     */
    suspend fun getRandomJoke(
        onJokeReceived: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // Create a client instance (dependencies will be injected by Koin)
        val client = ChuckNorrisClient()
        
        // Get a random joke
        client.getRandomJoke()
            .onSuccess { joke ->
                onJokeReceived(joke.value)
            }
            .onFailure { error ->
                onError(error)
            }
    }
    
    /**
     * Example of how to get available categories
     * 
     * @param onCategoriesReceived Callback for when categories are received
     * @param onError Callback for when an error occurs
     */
    suspend fun getCategories(
        onCategoriesReceived: (List<String>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // Create a client instance (dependencies will be injected by Koin)
        val client = ChuckNorrisClient()
        
        // Get categories
        client.getCategories()
            .onSuccess { categories ->
                onCategoriesReceived(categories)
            }
            .onFailure { error ->
                onError(error)
            }
    }
    
    /**
     * Example of how to search for jokes
     * 
     * @param query The search query
     * @param onJokesReceived Callback for when jokes are received
     * @param onError Callback for when an error occurs
     */
    suspend fun searchJokes(
        query: String,
        onJokesReceived: (List<String>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // Create a client instance (dependencies will be injected by Koin)
        val client = ChuckNorrisClient()
        
        // Search for jokes
        client.searchJokes(query)
            .onSuccess { jokes ->
                onJokesReceived(jokes.map { it.value })
            }
            .onFailure { error ->
                onError(error)
            }
    }
}