package io.github.kotlin.allfunds.networking.domain.usecase

import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository

/**
 * Use case for getting all available joke categories
 */
class GetCategoriesUseCase(
    private val repository: JokeRepository
) {
    /**
     * Execute the use case
     * @return Result containing a list of categories or an exception
     */
    suspend operator fun invoke(): Result<List<String>> {
        return repository.getCategories()
    }
}