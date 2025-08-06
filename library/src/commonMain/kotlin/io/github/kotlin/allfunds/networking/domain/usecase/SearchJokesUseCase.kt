package io.github.kotlin.allfunds.networking.domain.usecase

import io.github.kotlin.allfunds.networking.domain.model.Joke
import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository

/**
 * Use case for searching jokes
 */
class SearchJokesUseCase(
    private val repository: JokeRepository
) {
    /**
     * Execute the use case
     * @param query The search query
     * @return Result containing a list of jokes matching the query or an exception
     */
    suspend operator fun invoke(query: String): Result<List<Joke>> {
        if (query.length < 3) {
            return Result.failure(IllegalArgumentException("Search query must be at least 3 characters long"))
        }
        return repository.searchJokes(query)
    }
}