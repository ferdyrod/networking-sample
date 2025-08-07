
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinx.serialization)
    `maven-publish`
    signing
}

group = "io.github.kotlin"
version = "1.0.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // Name of the module to be imported in the consumer project
    val xcframeworkName = "networking"
    val xcf = XCFramework(xcframeworkName)

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = xcframeworkName

            binaryOption("bundleId", "com.allfunds.$xcframeworkName")
            xcf.add(this)
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.koin.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.koin.test)
            }
        }
    }
}

android {
    namespace = "io.github.kotlin.allfunds.networking"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kotlin/allfunds-networking")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    
    publications.withType<MavenPublication> {
        pom {
            name.set("Allfunds Networking")
            description.set("Kotlin Multiplatform library for networking")
            url.set("https://github.com/kotlin/allfunds-networking")
            
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            
            developers {
                developer {
                    id.set("allfunds")
                    name.set("Allfunds")
                    email.set("info@allfunds.com")
                }
            }
            
            scm {
                connection.set("scm:git:git://github.com/kotlin/allfunds-networking.git")
                developerConnection.set("scm:git:ssh://github.com/kotlin/allfunds-networking.git")
                url.set("https://github.com/kotlin/allfunds-networking")
            }
        }
    }
}

// Swift Package Manager export configuration
val xcframeworkPath = "build/XCFrameworks/release"
val swiftPackageDir = "swift-package"
val frameworkName = "networking"
val frameworkZipName = "$frameworkName.xcframework.zip"
val frameworkZipPath = "$swiftPackageDir/$frameworkZipName"
val repoOwner = "kotlin"
val repoName = "allfunds-networking"
val frameworkVersion = project.version.toString()
val frameworkUrl = "https://github.com/$repoOwner/$repoName/releases/download/v$frameworkVersion/$frameworkZipName"

tasks.register("createSwiftPackage") {
    // We're not using dependsOn here to allow the task to run for testing even without building the framework
    
    doLast {
        // Create Swift Package directory structure
        file(swiftPackageDir).mkdirs()
        
        // Check if XCFramework exists
        val xcFrameworkDir = file(xcframeworkPath)
        if (!xcFrameworkDir.exists()) {
            logger.warn("XCFramework directory does not exist at $xcframeworkPath")
            logger.warn("Creating a dummy framework structure for testing purposes")
            
            // Create dummy framework structure
            val dummyFrameworkDir = file("$swiftPackageDir/networking.xcframework")
            dummyFrameworkDir.mkdirs()
            file("$dummyFrameworkDir/Info.plist").writeText("<plist version=\"1.0\"><dict><key>CFBundleExecutable</key><string>networking</string></dict></plist>")
            
            // Create dummy zip file
            ant.withGroovyBuilder {
                "zip"("destfile" to frameworkZipPath, "basedir" to swiftPackageDir, "includes" to "networking.xcframework/**")
            }
        } else {
            // Create zip file of the real XCFramework
            ant.withGroovyBuilder {
                "zip"("destfile" to frameworkZipPath, "basedir" to xcframeworkPath)
            }
        }
        
        // Use a placeholder checksum - this needs to be replaced with the actual SHA-256 checksum
        // To generate the actual checksum, after creating the zip file:
        // - On macOS/Linux: shasum -a 256 swift-package/networking.xcframework.zip
        // - On Windows: certutil -hashfile swift-package/networking.xcframework.zip SHA256
        val checksum = "0000000000000000000000000000000000000000000000000000000000000000" // Placeholder
        
        // Write instructions to a file for reference
        val instructionsFile = file("$swiftPackageDir/UPDATE_CHECKSUM.txt")
        instructionsFile.writeText("""
            To update the checksum in Package.swift:
            
            1. Generate the SHA-256 checksum of the framework zip file:
               - On macOS/Linux: shasum -a 256 $frameworkZipName
               - On Windows: certutil -hashfile $frameworkZipName SHA256
               
            2. Replace the placeholder checksum in Package.swift with the generated value
            
            Current URL: $frameworkUrl
        """.trimIndent())
        
        // Create Package.swift file with URL and checksum
        val packageSwift = """
            // swift-tools-version:5.3
            import PackageDescription

            let package = Package(
                name: "networking",
                platforms: [
                    .iOS(.v14)
                ],
                products: [
                    .library(
                        name: "networking",
                        targets: ["networking"]
                    ),
                ],
                targets: [
                    .binaryTarget(
                        name: "networking",
                        url: "$frameworkUrl",
                        checksum: "$checksum"
                    ),
                ]
            )
        """.trimIndent()
        
        println("Created Swift Package with framework URL: $frameworkUrl")
        println("Framework checksum: $checksum")
        
        file("$swiftPackageDir/Package.swift").writeText(packageSwift)
        
        // Copy XCFramework to Swift Package directory for local development
        copy {
            from("$xcframeworkPath/networking.xcframework")
            into(swiftPackageDir)
        }
        
        // Create README.md file
        val readmeMd = """
            # Allfunds Networking

            A Kotlin Multiplatform library for networking that supports both iOS and Android platforms.

            ## Installation

            ### Swift Package Manager

            Add the following dependency to your Package.swift file:

            ```swift
            .package(url: "https://github.com/$repoOwner/$repoName.git", from: "$frameworkVersion")
            ```

            ### Gradle (Android)

            ```kotlin
            implementation("io.github.kotlin:allfunds-networking:$frameworkVersion")
            ```
            
            ## Framework Information
            
            - URL: $frameworkUrl
            - Checksum: $checksum
        """.trimIndent()
        
        file("$swiftPackageDir/README.md").writeText(readmeMd)
        
        println("Created Swift Package with framework URL: $frameworkUrl")
        println("Framework checksum: $checksum")
    }
}
