package io.github.kotlin.allfunds.networking.data.repository

import io.github.kotlin.allfunds.networking.data.remote.ChuckNorrisApi
import io.github.kotlin.allfunds.networking.data.remote.dto.JokeDto
import io.github.kotlin.allfunds.networking.data.remote.dto.SearchResponseDto
import io.github.kotlin.allfunds.networking.domain.model.Joke
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JokeRepositoryImplTest {
    
    private val mockApi = MockChuckNorrisApi()
    private val repository = JokeRepositoryImpl(mockApi)
    
    @Test
    fun getRandomJokeReturnsSuccessWithJoke() = runTest {
        val result = repository.getRandomJoke()
        
        assertTrue(result.isSuccess)
        result.onSuccess { joke ->
            assertEquals("test-id", joke.id)
            assertEquals("Test joke", joke.value)
            assertEquals("https://api.chucknorris.io/jokes/test-id", joke.url)
            assertEquals(listOf("test"), joke.categories)
        }
    }
    
    @Test
    fun getRandomJokeByCategoryReturnsSuccessWithJoke() = runTest {
        val result = repository.getRandomJokeByCategory("test")
        
        assertTrue(result.isSuccess)
        result.onSuccess { joke ->
            assertEquals("test-id", joke.id)
            assertEquals("Test joke", joke.value)
            assertEquals("https://api.chucknorris.io/jokes/test-id", joke.url)
            assertEquals(listOf("test"), joke.categories)
        }
    }
    
    @Test
    fun getCategoriesReturnsSuccessWithCategories() = runTest {
        val result = repository.getCategories()
        
        assertTrue(result.isSuccess)
        result.onSuccess { categories ->
            assertEquals(listOf("test", "dev"), categories)
        }
    }
    
    @Test
    fun searchJokesReturnsSuccessWithJokes() = runTest {
        val result = repository.searchJokes("test")
        
        assertTrue(result.isSuccess)
        result.onSuccess { jokes ->
            assertEquals(1, jokes.size)
            assertEquals("test-id", jokes[0].id)
            assertEquals("Test joke", jokes[0].value)
            assertEquals("https://api.chucknorris.io/jokes/test-id", jokes[0].url)
            assertEquals(listOf("test"), jokes[0].categories)
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