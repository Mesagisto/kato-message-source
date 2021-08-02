group = "org.meowcat"
version = "1.0.0"

plugins {
   java
   kotlin("jvm") version "1.4.20"
   id("io.izzel.taboolib") version "2.1"
}
repositories {
   mavenCentral()

   maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
   maven("https://jitpack.io")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

taboolib {
   description {
      contributors {
         name("Itsusinn逸新").description("Mesagisto Maintainer")
      }
   }
   version = "6.0.0-pre5"
   install("common", "common-5")
   install("platform-bukkit")
   install("module-configuration", "module-lang", "module-chat")
}

dependencies {
   compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.4.20")
   taboo("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
   taboo("org.jetbrains.kotlin:kotlin-reflect:1.4.20")
   compileOnly("org.spigotmc:spigot-api:1.12-R0.1-SNAPSHOT")

   compileOnly("net.md-5:bungeecord-chat:1.16-R0.3")
   taboo("org.meowcat:handy-dandy:0.1.0")

   taboo("io.nats:jnats:2.10.0")

   taboo("com.fasterxml.jackson.core:jackson-core:2.12.1")
   taboo("com.fasterxml.jackson.core:jackson-databind:2.12.1")
   taboo("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
   taboo("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")
}
