import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "vyfor"
version = "1.0"

repositories {
    mavenLocal()
    maven {
        url = uri("file://$projectDir/.m2/repository")
    }
    maven("https://europe-west3-maven.pkg.dev/mik-music/kord")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

kotlin {
    val mingwTarget = mingwX64()
    val linuxTarget = linuxX64()

    targets.withType<KotlinNativeTarget> {
        binaries {
            executable(listOf(RELEASE)) {
                entryPoint = "vyfor.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
                implementation("io.ktor:ktor-client-core:3.0.0-beta-2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val nativeMain by creating {
            dependsOn(commonMain)

            dependencies {
                implementation("io.github.jan-tennert.supabase:supabase-kt:2.5.4-wasm0")
                implementation("io.github.jan-tennert.supabase:postgrest-kt:2.5.4-wasm0")
                implementation("io.github.reactivecircus.cache4k:cache4k:0.13.0")

                implementation("dev.kord:kord-core:feature-native-SNAPSHOT")
                // implementation("dev.kord.x:emoji:feature-mpp-SNAPSHOT")
                // implementation("io.github.vyfor:cordex:0.1.0")
            }
        }
        val mingwX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-winhttp:3.0.0-beta-2")
            }
        }
        val linuxX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:3.0.0-beta-2")
            }
        }

        mingwTarget.apply {
            compilations["main"].defaultSourceSet.apply {
                dependsOn(nativeMain)
                dependsOn(mingwX64Main)
            }

            compilations["test"].defaultSourceSet.dependsOn(commonTest)
        }

        linuxTarget.apply {
            compilations["main"].defaultSourceSet.apply {
                dependsOn(nativeMain)
                dependsOn(linuxX64Main)
            }

            compilations["test"].defaultSourceSet.dependsOn(commonTest)
        }

        tasks.withType<Test> {
            testLogging {
                showStandardStreams = true
            }
        }
    }
}
