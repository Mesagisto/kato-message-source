group = "org.meowcat"
version = "1.0.0"

plugins {
  java
  kotlin("jvm") version "1.5.21"
  kotlin("plugin.serialization") version "1.5.21"
  id("com.github.johnrengelman.shadow") version "6.0.0"
  id("org.meowcat.kato") version "0.1.0-dev19"
}
repositories {
  mavenCentral()

  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
  maven("https://jitpack.io")
}
kato {
  exclude("META-INF/*.kotlin_module")
  exclude("*.md")
  exclude("DebugProbesKt.bin")
  excludePathStartWith("META-INF/maven")
  shadowJar {
    minimize()
  }
}

tasks.compileKotlin {
  kotlinOptions {
    jvmTarget = "11"
    freeCompilerArgs = listOf("-Xinline-classes", "-Xopt-in=kotlin.RequiresOptIn")
  }
  sourceCompatibility = "11"
}

dependencies {
  compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
  implementation("io.nats:jnats:2.12.0")
  implementation("org.meowcat:mesagisto-client-jvm:1.0.5")
  implementation("io.arrow-kt:arrow-core:1.0.0")
  compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
}

kotlin.target.compilations.all {
  kotlinOptions {
    apiVersion = "1.5"
    languageVersion = "1.5"
  }
}
