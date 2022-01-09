group = "org.meowcat"
version = property("project_version")!!
plugins {
  java
  kotlin("jvm") version "1.5.21"
  kotlin("plugin.serialization") version "1.5.21"
  id("com.github.johnrengelman.shadow") version "6.0.0"
  id("io.itsusinn.pkg") version "1.0.0"
}
repositories {
  mavenCentral()
  maven("https://oss.sonatype.org/content/repositories/snapshots/")
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
java {
  targetCompatibility = JavaVersion.VERSION_1_8
}
tasks.compileKotlin {
  kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = listOf("-Xinline-classes", "-Xopt-in=kotlin.RequiresOptIn")
  }
  sourceCompatibility = "1.8"
}

dependencies {
  compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
  implementation("io.nats:jnats:2.13.1")
  implementation("org.meowcat:mesagisto-client-jvm:1.0.12")
  implementation("io.arrow-kt:arrow-core:1.0.0")
  compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
}
tasks {
  processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") {
      expand(mutableMapOf("version" to project.version))
    }
  }
  compileKotlin {
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs = listOf("-Xinline-classes", "-Xopt-in=kotlin.RequiresOptIn")
    }
    sourceCompatibility = "1.8"
  }
}
