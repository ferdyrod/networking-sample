package io.github.kotlin.allfunds.networking.domain.usecase

import io.github.kotlin.allfunds.networking.domain.model.Joke
import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetRandomJokeUseCaseTest {
    
    private val mockRepository = MockJokeRepository()
    private val useCase = GetRandomJokeUseCase(mockRepository)
    
    @Test
    fun invokeReturnsSuccessWithJoke() = runTest {
        val result = useCase()
        
        assertTrue(result.isSuccess)
        result.onSuccess { joke ->
            assertEquals("test-id", joke.id)
            assertEquals("Test joke", joke.value)
            assertEquals("https://api.chucknorris.io/jokes/test-id", joke.url)
            assertEquals(listOf("test"), joke.categories)
        }
    }
    
    // Mock implementation of JokeRepository for testing
    private class MockJokeRepository : JokeRepository {
        override suspend fun getRandomJoke(): Result<Joke> {
            return Result.success(
                Joke(
                    id = "test-id",
                    value = "Test joke",
                    url = "https://api.chucknorris.io/jokes/test-id",
                    categories = listOf("test")
                )
            )
        }
        
        override suspend fun getRandomJokeByCategory(category: String): Result<Joke> {
            return Result.success(
                Joke(
                    id = "test-id",
                    value = "Test joke",
                    url = "https://api.chucknorris.io/jokes/test-id",
                    categories = listOf(category)
                )
            )
        }
        
        override suspend fun getCategories(): Result<List<String>> {
            return Result.success(listOf("test", "dev"))
        }
        
        override suspend fun searchJokes(query: String): Result<List<Joke>> {
            return Result.success(
                listOf(
                    Joke(
                        id = "test-id",
                        value = "Test joke",
                        url = "https://api.chucknorris.io/jokes/test-id",
                        categories = listOf("test")
                    )
                )
            )
        }
    }
}