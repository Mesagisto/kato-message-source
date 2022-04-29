import io.itsusinn.pkg.pkgIn

group = "org.meowcat"
version = property("project_version")!!
plugins {
  java
  kotlin("jvm") version "1.6.0"
  kotlin("plugin.serialization") version "1.6.0"
  id("com.github.johnrengelman.shadow") version "7.1.1"
  id("io.itsusinn.pkg") version "1.2.0"
}
repositories {
  mavenCentral()
  mavenLocal()

  maven("https://oss.sonatype.org/content/repositories/snapshots/")
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
  maven("https://jitpack.io")
}
pkg {
  excludePath("META-INF/*.kotlin_module")
  excludePath("*.md")
  excludePath("DebugProbesKt.bin")
  excludePathStartWith("META-INF/maven")
  shadowJar {
    minimize()
    mergeServiceFiles()
  }
  relocateKotlinStdlib()
  relocateKotlinxLib()
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
  pkgIn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
  pkgIn("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.2.2")
  pkgIn("io.nats:jnats:2.14.0")
  pkgIn("org.meowcat:mesagisto-client-jvm:1.3.0")
  // implementation("org.meowcat:mesagisto-client:1.1.1-n4")
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
