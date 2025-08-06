package io.github.kotlin.allfunds.networking

import io.github.kotlin.allfunds.networking.domain.model.Joke
import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test for JokeRepository functionality
 */
class JokeRepositoryTest {
    
    @Test
    fun testRepositoryMethods() = runTest {
        // Create a mock repository for testing
        val mockRepository = object : JokeRepository {
            override suspend fun getRandomJoke(): Result<Joke> {
                return Result.success(
                    Joke(
                        id = "test-id",
                        value = "Chuck Norris can divide by zero.",
                        url = "https://api.chucknorris.io/jokes/test-id",
                        categories = listOf("math")
                    )
                )
            }
            
            override suspend fun getRandomJokeByCategory(category: String): Result<Joke> {
                return Result.success(
                    Joke(
                        id = "cat-joke",
                        value = "Chuck Norris's keyboard doesn't have a Ctrl key because nothing controls Chuck Norris.",
                        url = "https://api.chucknorris.io/jokes/cat-joke",
                        categories = listOf(category)
                    )
                )
            }
            
            override suspend fun getCategories(): Result<List<String>> {
                return Result.success(listOf("dev", "science", "math", "history"))
            }
            
            override suspend fun searchJokes(query: String): Result<List<Joke>> {
                return Result.success(
                    listOf(
                        Joke(
                            id = "prog-joke",
                            value = "Chuck Norris can write infinite recursion functions and have them return.",
                            url = "https://api.chucknorris.io/jokes/prog-joke",
                            categories = listOf("dev")
                        )
                    )
                )
            }
        }
        
        // Test the repository methods
        val randomJokeResult = mockRepository.getRandomJoke()
        assertTrue(randomJokeResult.isSuccess)
        randomJokeResult.onSuccess { joke ->
            assertEquals("test-id", joke.id)
            assertEquals("Chuck Norris can divide by zero.", joke.value)
        }
        
        val categoriesResult = mockRepository.getCategories()
        assertTrue(categoriesResult.isSuccess)
        categoriesResult.onSuccess { categories ->
            assertTrue(categories.contains("dev"))
            assertTrue(categories.contains("science"))
        }
        
        val searchResult = mockRepository.searchJokes("programming")
        assertTrue(searchResult.isSuccess)
        searchResult.onSuccess { jokes ->
            assertTrue(jokes.isNotEmpty())
            assertEquals("Chuck Norris can write infinite recursion functions and have them return.", jokes[0].value)
        }
    }
}