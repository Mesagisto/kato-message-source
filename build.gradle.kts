group = "org.meowcat"
version = property("project_version")!!
plugins {
  java
  kotlin("jvm") version "1.5.21"
  kotlin("plugin.serialization") version "1.5.21"
  id("com.github.johnrengelman.shadow") version "7.1.1"
  id("io.itsusinn.pkg") version "1.0.0"
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
  excludePathStartWith("org/bouncycastle")
  excludePathStartWith("org/slf4j")
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
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.3.2")
  implementation("io.nats:jnats:2.13.1")
  implementation("org.meowcat:mesagisto-client-jvm:1.1.2")
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
