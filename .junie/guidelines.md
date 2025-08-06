# Allfunds Networking Project Guidelines

This document provides essential information for developers working on the Allfunds Networking project, a Kotlin Multiplatform library that implements the data layer for Android and iOS applications.

## Build/Configuration Instructions

### Prerequisites
- JDK 17 or higher
- Android SDK (for Android target)
- Xcode (for iOS targets)

### Project Setup
1. Clone the repository
2. Open the project in IntelliJ IDEA or Android Studio

### Building the Project
The project uses Gradle as its build system. Here are the key commands:

```bash
# Build all targets
./gradlew build

# Build specific target
./gradlew :library:androidMainClasses  # Android
./gradlew :library:iosX64MainClasses  # iOS X64
```

### Publishing
The project is configured to publish to GitHub Packages and Swift Package Manager:

#### Publishing to GitHub Packages (for Android)

1. Configure your GitHub credentials in your environment:
   ```properties
   GITHUB_TOKEN=your_github_token
   ```

2. Update the POM information in `library/build.gradle.kts` with your project details:
   - Group ID (currently `io.github.kotlin`)
   - Artifact ID (currently `allfunds-networking`)
   - Version (currently `1.0.0`)
   - License information
   - Developer information
   - SCM information

3. Publish to GitHub Packages:
   ```bash
   ./gradlew publishAllPublicationsToGitHubPackagesRepository
   ```

#### Publishing for iOS (Swift Package Manager)

1. Generate the Swift Package:
   ```bash
   ./gradlew createSwiftPackage
   ```

2. Commit and push the generated Swift Package to your repository.

3. iOS developers can then add the package to their project using Swift Package Manager by specifying your GitHub repository URL.

## Testing Information

### Test Structure
- **Common Tests**: Located in `library/src/commonTest/kotlin/` - run on all platforms
- **Platform-Specific Tests**: Located in platform-specific directories:
  - Android: `library/src/androidUnitTest/kotlin/`
  - iOS: `library/src/iosTest/kotlin/`

### Running Tests
```bash
# Run all tests
./gradlew allTests

# Run platform-specific tests
./gradlew androidTest
./gradlew iosX64Test
```

### Adding New Tests
1. Create a test class in the appropriate test directory
2. Use the Kotlin Test framework (`kotlin.test` package)
3. Annotate test methods with `@Test`

#### Example Test
```kotlin
class ApiClientTest {
    @Test
    fun testSuccessfulResponse() {
        val apiClient = ApiClient()
        val response = apiClient.getTestData()
        
        assertEquals(200, response.statusCode)
        assertEquals("Success", response.message)
    }
}
```

## Development Information

### Project Structure
- **Common Code**: `library/src/commonMain/kotlin/` - shared across all platforms
- **Platform-Specific Code**: 
  - Android: `library/src/androidMain/kotlin/`
  - iOS: `library/src/iosMain/kotlin/`

### Expect/Actual Pattern
The project uses Kotlin's expect/actual pattern for platform-specific implementations:

1. Define expected declarations in common code:
   ```
   // In commonMain
   expect class HttpClient() {
       suspend fun request(url: String): Response
   }
   ```

2. Provide actual implementations in platform-specific code:
   ```
   // In androidMain
   actual class HttpClient {
       actual suspend fun request(url: String): Response {
           // Android-specific implementation
       }
   }
   ```

### Key Dependencies
- **Ktor**: HTTP client for network requests
- **Kotlinx Serialization**: JSON parsing
- **Kotlinx Coroutines**: Asynchronous programming
- **Kotlinx DateTime**: Date and time handling
- **Koin**: Dependency injection framework

### Dependency Injection with Koin
The project uses Koin for dependency injection. Here's how it's set up:

1. **Module Definition**: Dependencies are defined in `NetworkModule.kt`:
   ```kotlin
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
   ```

2. **Initialization**: Koin is initialized using `KoinInitializer`:
   ```kotlin
   // Initialize Koin with the network module
   KoinInitializer.init()
   ```

3. **Dependency Injection**: Classes use Koin to inject dependencies:
   ```kotlin
   class ChuckNorrisClient : KoinComponent {
       // Inject use cases from Koin
       private val getRandomJokeUseCase: GetRandomJokeUseCase by inject()
       // ...
   }
   ```

4. **Testing with Koin**: For testing, you can provide mock dependencies:
   ```kotlin
   @BeforeTest
   fun setup() {
       startKoin {
           modules(
               module {
                   single<ChuckNorrisApi> { MockChuckNorrisApi() }
                   // ...
               }
           )
       }
   }
   ```

### Versioning
- Version is defined in `library/build.gradle.kts`
- Follow semantic versioning (MAJOR.MINOR.PATCH)

### CI/CD
- GitHub Actions workflows are configured in `.github/workflows/`
- `gradle.yml`: Builds and tests the project
- `publish.yml`: Publishes the library to GitHub Packages and generates Swift Package

### Code Style
- Follow Kotlin coding conventions
- Use meaningful names for classes, functions, and variables
- Write comprehensive tests for all functionality
- Document public APIs with KDoc comments