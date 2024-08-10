import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "2.0.0"
}

group = "vyfor"
version = "0.1.0"

repositories {
    maven("https://europe-west3-maven.pkg.dev/mik-music/kord")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
}

kotlin {
    val mingwTarget = mingwX64()
    val linuxTargets = arrayOf(
        linuxX64(),
        linuxArm64()
    )
    val macosTargets = arrayOf(
        macosX64(),
        macosArm64()
    )

    targets.withType<KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "vyfor.main"
            }
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // implementation("io.github.jan-tennert.supabase:supabase-kt:2.5.4-wasm0")
                // implementation("io.github.jan-tennert.supabase:postgrest-kt:2.5.4-wasm0")
                implementation("dev.kord:kord-core:feature-native-SNAPSHOT")
                // implementation("dev.kord.x:emoji:feature-mpp-SNAPSHOT")
            }
        }
        val mingwX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-winhttp:3.0.0-beta-2")
            }
        }
        val linuxX64Main by getting {
            dependencies {
                // implementation("io.ktor:ktor-client-curl:3.0.0-beta-2")
            }
        }
        val linuxArm64Main by getting {
            dependencies {
                // implementation("io.ktor:ktor-client-curl:3.0.0-beta-2")
            }
        }
        mingwTarget.apply {
            compilations["main"].defaultSourceSet.apply {
                dependsOn(commonMain)
            }
        }
        linuxTargets.forEach {
            it.apply {
                compilations["main"].defaultSourceSet.apply {
                    dependsOn(commonMain)
                }
            }
        }
        macosTargets.forEach {
            it.apply {
                compilations["main"].defaultSourceSet.apply {
                    dependsOn(commonMain)
                }
            }
        }
    }
}
