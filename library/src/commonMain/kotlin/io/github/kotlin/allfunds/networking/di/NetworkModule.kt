package io.github.kotlin.allfunds.networking.di

import io.github.kotlin.allfunds.networking.data.remote.ChuckNorrisApi
import io.github.kotlin.allfunds.networking.data.remote.ChuckNorrisApiImpl
import io.github.kotlin.allfunds.networking.data.repository.JokeRepositoryImpl
import io.github.kotlin.allfunds.networking.domain.repository.JokeRepository
import io.github.kotlin.allfunds.networking.domain.usecase.GetCategoriesUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.GetRandomJokeByCategoryUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.GetRandomJokeUseCase
import io.github.kotlin.allfunds.networking.domain.usecase.SearchJokesUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for network dependencies
 */
val networkModule = module {
    // API
    single<ChuckNorrisApi> { ChuckNorrisApiImpl() }
    
    // Repository
    single<JokeRepository> { JokeRepositoryImpl(get()) }
    
    // Use Cases
    factory { GetRandomJokeUseCase(get()) }
    factory { GetRandomJokeByCategoryUseCase(get()) }
    factory { GetCategoriesUseCase(get()) }
    factory { SearchJokesUseCase(get()) }
}