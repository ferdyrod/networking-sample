package io.github.kotlin.allfunds.networking

import io.github.kotlin.allfunds.networking.data.remote.ChuckNorrisApi
import io.github.kotlin.allfunds.networking.data.remote.dto.JokeDto
import io.github.kotlin.allfunds.networking.data.remote.dto.SearchResponseDto
import io.github.kotlin.allfunds.networking.data.repository.JokeRepositoryImpl
import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test for ChuckNorrisClient with Koin dependency injection
 */
class KoinChuckNorrisClientTest : KoinTest {
    
    @BeforeTest
    fun setup() {
        // Start Koin with test module
        startKoin {
            modules(
                module {
                    // Mock API
                    single<ChuckNorrisApi> { MockChuckNorrisApi() }
                    // Real repository with mock API
                    single<JokeRepository> { JokeRepositoryImpl(get()) }
                    // Use cases
                    factory { io.github.kotlin.allfunds.networking.domain.usecase.GetRandomJokeUseCase(get()) }
                    factory { io.github.kotlin.allfunds.networking.domain.usecase.GetRandomJokeByCategoryUseCase(get()) }
                    factory { io.github.kotlin.allfunds.networking.domain.usecase.GetCategoriesUseCase(get()) }
                    factory { io.github.kotlin.allfunds.networking.domain.usecase.SearchJokesUseCase(get()) }
                }
            )
        }
    }
    
    @AfterTest
    fun tearDown() {
        // Stop Koin after each test
        stopKoin()
    }
    
    @Test
    fun testGetRandomJoke() = runTest {
        // Create client that will use Koin for dependency injection
        val client = ChuckNorrisClient()
        
        // Test getRandomJoke
        val result = client.getRandomJoke()
        
        assertTrue(result.isSuccess)
        result.onSuccess { joke ->
            assertEquals("test-id", joke.id)
            assertEquals("Test joke", joke.value)
        }
    }
    
    @Test
    fun testGetCategories() = runTest {
        // Create client that will use Koin for dependency injection
        val client = ChuckNorrisClient()
        
        // Test getCategories
        val result = client.getCategories()
        
        assertTrue(result.isSuccess)
        result.onSuccess { categories ->
            assertEquals(2, categories.size)
            assertTrue(categories.contains("test"))
            assertTrue(categories.contains("dev"))
        }
    }
    
    // Mock implementation of ChuckNorrisApi for testing
    private class MockChuckNorrisApi : ChuckNorrisApi {
        override suspend fun getRandomJoke(): JokeDto {
            return JokeDto(
                id = "test-id",
                value = "Test joke",
                url = "https://api.chucknorris.io/jokes/test-id",
                categories = listOf("test")
            )
        }
        
        override suspend fun getRandomJokeByCategory(category: String): JokeDto {
            return JokeDto(
                id = "test-id",
                value = "Test joke",
                url = "https://api.chucknorris.io/jokes/test-id",
                categories = listOf("test")
            )
        }
        
        override suspend fun getCategories(): List<String> {
            return listOf("test", "dev")
        }
        
        override suspend fun searchJokes(query: String): SearchResponseDto {
            return SearchResponseDto(
                total = 1,
                result = listOf(
                    JokeDto(
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