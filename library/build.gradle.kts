
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

group = "io.kotlin.networking-sample"
version = "0.0.03"

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

        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
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
            url = uri("https://maven.pkg.github.com/ferdyrod/networking-sample")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("ACCESS_TOKEN")
            }
        }
    }
    
    publications.withType<MavenPublication> {
        pom {
            name.set("Allfunds Networking Sample")
            description.set("Kotlin Multiplatform library for networking")
            url.set("https://github.com/ferdyrod/networking-sample")
            
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            
            developers {
                developer {
                    id.set("test")
                    name.set("test")
                    email.set("frodriguez@quadram.mobi")
                }
            }
            
            scm {
                connection.set("scm:git:git://github.com/ferdyrod/networking-sample.git")
                developerConnection.set("scm:git:ssh://github.com/ferdyrod/networking-sample.git")
                url.set("https://github.com/ferdyrod/networking-sample")
            }
        }
    }
}

// Swift Package Manager export configuration
val xcframeworkPath = "build/XCFrameworks/release"
val swiftPackageDir = "swift-package"

tasks.register("createSwiftPackage") {
    dependsOn("assembleNetworkingXCFramework")
    
    doLast {
        // Create Swift Package directory structure
        file(swiftPackageDir).mkdirs()
        
        // Create Package.swift file
        val packageSwift = """
            // swift-tools-version:5.9
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
                        url: "",
                        checksum: ""
                    ),
                ]
            )
        """.trimIndent()
        
        file("$swiftPackageDir/Package.swift").writeText(packageSwift)
        
        // Copy XCFramework to Swift Package directory
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
            .package(url: "https://github.com/kotlin/allfunds-networking.git", from: "1.0.0")
            ```

            ### Gradle (Android)

            ```kotlin
            implementation("io.github.kotlin:allfunds-networking:1.0.0")
            ```
        """.trimIndent()
        
        file("$swiftPackageDir/README.md").writeText(readmeMd)
    }
}
