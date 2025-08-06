package io.github.kotlin.allfunds.networking.domain.model

/**
 * Domain model representing a Chuck Norris joke
 */
data class Joke(
    val id: String,
    val value: String,
    val url: String,
    val categories: List<String>
)