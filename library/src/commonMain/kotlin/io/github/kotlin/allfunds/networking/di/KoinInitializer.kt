package io.github.kotlin.allfunds.networking.di

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.KoinApplication

/**
 * Initializer for Koin dependency injection
 */
object KoinInitializer {
    private var initialized = false
    
    /**
     * Initialize Koin with the network module
     * @return KoinApplication instance
     */
    fun init(): KoinApplication {
        if (initialized) {
            stopKoin()
        }
        
        val koinApp = startKoin {
            modules(networkModule)
        }
        
        initialized = true
        return koinApp
    }
    
    /**
     * Check if Koin is initialized
     * @return true if Koin is initialized, false otherwise
     */
    fun isInitialized(): Boolean = initialized
}