package io.github.kotlin.allfunds.networking.domain.usecase

import io.github.kotlin.allfunds.networking.domain.model.Joke
import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository

/**
 * Use case for getting a random joke from a specific category
 */
class GetRandomJokeByCategoryUseCase(
    private val repository: JokeRepository
) {
    /**
     * Execute the use case
     * @param category The category to get a joke from
     * @return Result containing a random joke from the specified category or an exception
     */
    suspend operator fun invoke(category: String): Result<Joke> {
        return repository.getRandomJokeByCategory(category)
    }
}