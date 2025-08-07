# Chuck Norris KMP Library - Guía de Solución de Problemas

## 📋 Descripción
Librería Kotlin Multiplatform (KMP) que proporciona acceso a la API de Chuck Norris usando Ktor para networking y Koin para inyección de dependencias.

## 🚀 Configuración Inicial

### Prerrequisitos
- Kotlin 2.2.0+
- Ktor 3.2.3+
- Koin 3.5.3+
- Xcode 15+
- iOS 14+

### Estructura del Proyecto
```
networking-sample/
├── library/
│   ├── src/
│   │   ├── commonMain/kotlin/     # Código compartido
│   │   ├── iosMain/kotlin/        # Implementación específica iOS
│   │   └── androidMain/kotlin/    # Implementación específica Android
│   └── build.gradle.kts
├── library-test/                   # Solo para desarrollo local
│   ├── Package.swift
│   └── networking.xcframework/     # Temporal durante desarrollo
└── README.md
```

## 🔧 Configuración de Dependencias

### build.gradle.kts
```kotlin
sourceSets {
    val commonMain by getting {
        dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.koin.core)
            // CRÍTICO: Motor HTTP para iOS
            implementation("io.ktor:ktor-client-darwin:3.2.3")
        }
    }
    
    val iosX64Main by getting {
        dependencies {
            implementation("io.ktor:ktor-client-darwin:3.2.3")
        }
    }
    
    val iosArm64Main by getting {
        dependencies {
            implementation("io.ktor:ktor-client-darwin:3.2.3")
        }
    }
    
    val iosSimulatorArm64Main by getting {
        dependencies {
            implementation("io.ktor:ktor-client-darwin:3.2.3")
        }
    }
    
    val androidMain by getting {
        dependencies {
            implementation("io.ktor:ktor-client-android:3.2.3")
        }
    }
}
```

## ⚠️ Problemas Comunes y Soluciones

### 1. Error: "Failed to find HTTP client engine implementation"

**Síntomas:**
```
Exception doesn't match @Throws-specified class list and thus isn't propagated from Kotlin to Objective-C/Swift as NSError.
Uncaught Kotlin exception: kotlin.native.internal.FileFailedToInitializeException
Caused by: kotlin.IllegalStateException: Failed to find HTTP client engine implementation
```

**Causa:** Ktor no puede encontrar el motor HTTP específico para la plataforma.

**Solución:**
1. **Agregar motor Darwin en commonMain:**
   ```kotlin
   implementation("io.ktor:ktor-client-darwin:3.2.3")
   ```

2. **Usar motor explícitamente en iOS:**
   ```kotlin
   private val client = HttpClient(Darwin) {
       install(ContentNegotiation) {
           json(Json { /* config */ })
       }
   }
   ```

3. **Recompilar y actualizar XCFramework:**
   ```bash
   ./gradlew :library:assembleNetworkingReleaseXCFramework
   ```

### 2. Error: "Exception doesn't match @Throws-specified class list"

**Síntomas:**
```
Exception doesn't match @Throws-specified class list and thus isn't propagated from Kotlin to Objective-C/Swift as NSError.
```

**Causa:** Las excepciones de Kotlin no se propagan automáticamente a Swift.

**Solución:**
1. **Agregar @Throws a todos los métodos:**
   ```kotlin
   @Throws(Exception::class)
   suspend fun getRandomJoke(): Joke
   ```

2. **Manejar errores explícitamente:**
   ```kotlin
   try {
       // código que puede fallar
   } catch (e: Throwable) {
       throw Exception("Mensaje descriptivo", e)
   }
   ```

3. **Usar tipos directos en lugar de Result<T>:**
   ```kotlin
   // ✅ Correcto
   @Throws(Exception::class)
   suspend fun getRandomJoke(): Joke
   
   // ❌ Evitar
   suspend fun getRandomJoke(): Result<Joke>
   ```

### 3. Error: "Cannot convert value of type 'Any?' to specified type"

**Síntomas:**
```
Cannot convert value of type 'Any?' to specified type '[Joke]'
```

**Causa:** Swift no puede inferir correctamente los tipos desde Kotlin.

**Solución:**
1. **Usar cast explícito en Swift:**
   ```swift
   let jokes = try await client.searchJokes(query: query) as! [Joke]
   ```

2. **O usar conversión manual:**
   ```swift
   var jokeStrings: [String] = []
   for joke in jokes {
       jokeStrings.append(joke.value)
   }
   ```

## 🔄 Flujo de Trabajo Correcto

### 1. Desarrollo en KMP
```bash
# Hacer cambios en el código Kotlin
# Recompilar
./gradlew :library:assembleNetworkingReleaseXCFramework
```

### 2. Verificación de Versiones
```bash
# Verificar que el XCFramework se generó correctamente
ls -la build/XCFrameworks/release/networking.xcframework/
```

### 3. Uso en Xcode
1. Limpiar proyecto (Product > Clean Build Folder)
2. Compilar y ejecutar
3. Verificar logs en consola

## 📱 Implementación en Swift

### Interfaz
```swift
protocol ChuckNorrisInterface {
    func getRandomJoke() async throws -> String
}

class ChuckNorrisInterfaceImpl: ChuckNorrisInterface {
    private let client = ChuckNorrisClient()
    
    func getRandomJoke() async throws -> String {
        do {
            let joke: Joke = try await client.getRandomJoke()
            return joke.value
        } catch {
            throw error
        }
    }
}
```

### ViewModel
```swift
@MainActor
class HomeViewModel: ObservableObject {
    @Published var randomJoke: String = ""
    @Published var isLoading = false
    @Published var errorMessage: String?
    
    private let chuckNorrisInterface: ChuckNorrisInterface
    
    func loadRandomJoke() async {
        isLoading = true
        errorMessage = nil
        
        do {
            randomJoke = try await chuckNorrisInterface.getRandomJoke()
        } catch {
            errorMessage = "Error: \(error.localizedDescription)"
        }
        
        isLoading = false
    }
}
```

## 🧪 Testing

### Verificar Inicialización
```swift
// En AppState.swift
class AppState: ObservableObject {
    @Published var isKMPInitialized = false
    
    private func initializeKMP() {
        do {
            SampleUsage.initialize()
            let _ = ChuckNorrisClient()
            isKMPInitialized = true
        } catch {
            print("❌ Error inicializando KMP: \(error)")
        }
    }
}
```

### Logs de Debugging
```swift
// Agregar logs detallados
print("🔧 ChuckNorrisInterfaceImpl.getRandomJoke() iniciado")
print("📞 Llamando a client.getRandomJoke()...")
print("📦 Joke recibido: \(joke)")
print("✅ Chiste cargado exitosamente: \(randomJoke)")
```

## 📋 Checklist de Verificación

### Antes de Compilar
- [ ] Motor Darwin agregado en commonMain
- [ ] @Throws(Exception::class) en todos los métodos
- [ ] Manejo de errores con try-catch
- [ ] Tipos de retorno directos (no Result<T>)

### Después de Compilar
- [ ] XCFramework generado correctamente
- [ ] Verificar estructura del framework
- [ ] Package.swift configurado (cuando esté publicado)

### En Xcode
- [ ] Proyecto limpiado
- [ ] Librería importada correctamente
- [ ] Logs de inicialización visibles
- [ ] Llamadas HTTP funcionando

## 🚨 Troubleshooting

### Si el error persiste:
1. **Verificar versiones de Ktor:**
   ```kotlin
   // Asegurar que todas las dependencias usen la misma versión
   implementation("io.ktor:ktor-client-darwin:3.2.3")
   implementation("io.ktor:ktor-client-core:3.2.3")
   ```

2. **Limpiar cache de Gradle:**
   ```bash
   ./gradlew clean
   ./gradlew :library:assembleNetworkingReleaseXCFramework
   ```

3. **Verificar estructura del XCFramework:**
   ```bash
   ls -la build/XCFrameworks/release/networking.xcframework/ios-arm64/networking.framework/
   # Debe contener el archivo 'networking' (librería compilada)
   ```

4. **Reiniciar Xcode completamente:**
   - Cerrar Xcode
   - Eliminar derived data
   - Reabrir proyecto

## 🏗️ Arquitectura del Proyecto

### Clean Architecture
```
library/src/
├── commonMain/kotlin/
│   ├── domain/
│   │   ├── model/           # Entidades de dominio
│   │   ├── repository/      # Interfaces de repositorio
│   │   └── usecase/         # Casos de uso
│   ├── data/
│   │   ├── remote/          # APIs y DTOs
│   │   ├── repository/      # Implementaciones de repositorio
│   │   └── mapper/          # Mappers DTO ↔ Domain
│   ├── di/                  # Inyección de dependencias (Koin)
│   └── ChuckNorrisClient.kt # Cliente principal
├── iosMain/kotlin/          # Implementaciones específicas iOS
└── androidMain/kotlin/      # Implementaciones específicas Android
```

### Patrón expect/actual
```kotlin
// commonMain
expect class ChuckNorrisApiImpl() : ChuckNorrisApi

// iosMain
actual class ChuckNorrisApiImpl : ChuckNorrisApi {
    private val client = HttpClient(Darwin) { /* config */ }
}

// androidMain
actual class ChuckNorrisApiImpl : ChuckNorrisApi {
    private val client = HttpClient { /* config */ }
}
```

## 📚 Referencias

- [Ktor HTTP Client Engines](https://ktor.io/docs/http-client-engines.html)
- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Koin Documentation](https://insert-koin.io/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## 🤝 Contribución

Para contribuir al proyecto:
1. Seguir el flujo de trabajo establecido
2. Agregar @Throws a nuevos métodos
3. Incluir motor HTTP específico para nuevas plataformas
4. Actualizar este README con nuevos problemas encontrados
5. Mantener la arquitectura limpia

## 📦 Distribución

### Para desarrollo local:
```bash
# Compilar
./gradlew :library:assembleNetworkingReleaseXCFramework
```

### Para distribución:
```bash
# Publicar en Maven Central
./gradlew :library:publishToMavenCentral

# O publicar y liberar automáticamente
./gradlew :library:publishAndReleaseToMavenCentral
```

### Uso cuando esté publicada:
```swift
// Package.swift
dependencies: [
    .package(url: "https://github.com/tu-usuario/networking-sample", from: "1.0.0")
]
```

La librería incluirá:
- ✅ URL con checksum para verificación de integridad
- ✅ Versiones semánticas (semver)
- ✅ Documentación automática
- ✅ Soporte para Swift Package Manager

---

**Última actualización:** Agosto 2025  
**Versión:** 1.0.0  
**Autor:** Equipo de Desarrollo
