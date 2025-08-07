package io.github.kotlin.allfunds.networking.data.remote

import io.github.kotlin.allfunds.networking.data.remote.dto.JokeDto
import io.github.kotlin.allfunds.networking.data.remote.dto.SearchResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Implementation of the Chuck Norris API
 * 
 * Note: This is an abstract class that will be implemented by platform-specific versions
 */
expect class ChuckNorrisApiImpl() : ChuckNorrisApi {
    @Throws(Exception::class)
    override suspend fun getRandomJoke(): JokeDto
    
    @Throws(Exception::class)
    override suspend fun getRandomJokeByCategory(category: String): JokeDto
    
    @Throws(Exception::class)
    override suspend fun getCategories(): List<String>
    
    @Throws(Exception::class)
    override suspend fun searchJokes(query: String): SearchResponseDto
}