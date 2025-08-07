package io.github.kotlin.allfunds.networking.di

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.KoinApplication
import kotlin.Throws

/**
 * Initializer for Koin dependency injection
 */
object KoinInitializer {
    private var initialized = false
    
    /**
     * Initialize Koin with the network module
     * @return KoinApplication instance
     * @throws IllegalStateException if Koin initialization fails
     */
    @Throws(IllegalStateException::class)
    fun init(): KoinApplication {
        try {
            if (initialized) {
                stopKoin()
            }
            
            val koinApp = startKoin {
                modules(networkModule)
            }
            
            initialized = true
            return koinApp
        } catch (e: Exception) {
            // Wrap any exception in IllegalStateException to ensure consistent error handling in Swift
            if (e !is IllegalStateException) {
                throw IllegalStateException("Failed to initialize Koin", e)
            }
            throw e
        }
    }
    
    /**
     * Check if Koin is initialized
     * @return true if Koin is initialized, false otherwise
     */
    fun isInitialized(): Boolean = initialized
}