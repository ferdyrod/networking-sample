package io.github.kotlin.allfunds.networking.domain.usecase

import io.github.kotlin.allfunds.networking.domain.model.Joke
import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository

/**
 * Use case for getting a random joke
 */
class GetRandomJokeUseCase(
    private val repository: JokeRepository
) {
    /**
     * Execute the use case
     * @return Result containing a random joke or an exception
     */
    suspend operator fun invoke(): Result<Joke> {
        return repository.getRandomJoke()
    }
}