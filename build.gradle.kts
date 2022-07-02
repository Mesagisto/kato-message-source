import io.itsusinn.pkg.pkgIn

group = "org.meowcat"
version = "1.3.3"
plugins {
  java
  kotlin("jvm") version "1.7.0"
  id("com.github.johnrengelman.shadow") version "7.1.1"
  id("io.itsusinn.pkg") version "1.2.2"
}
repositories {
  mavenCentral()
  mavenLocal()

  maven("https://oss.sonatype.org/content/repositories/snapshots/")
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}
pkg {
  excludePath("META-INF/*.kotlin_module")
  excludePathStartWith("META-INF/versions")
  excludePathStartWith("META-INF/proguard")
  excludePathStartWith("META-INF/maven")
  excludePathStartWith("org/slf4j")
  excludePathStartWith("org/jetbrains")
  excludePathStartWith("org/intellij")
  excludePath("*.md")
  excludePath("DebugProbesKt.bin")
  excludePathStartWith("kotlinx/coroutines/flow")
  listOf("asn1", "jcajce", "jce", "pqc", "x509", "math", "i18n", "iana", "internal").forEach {
    excludePathStartWith("org/bouncycastle/$it")
  }
  excludePathStartWith("META-INF/maven")
  listOf("stream", "math")
  shadowJar {
    minimize()
    mergeServiceFiles()
  }
  kotlinRelocate("org.yaml.snakeyaml","$group.relocate.org.yaml.snakeyaml")
}
java {
  targetCompatibility = JavaVersion.VERSION_1_8
  sourceCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  pkgIn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2") {
    exclude(group = "org.jetbrains.kotlin")
  }
  pkgIn("io.nats:jnats:2.15.3")
  pkgIn("org.mesagisto:mesagisto-client:1.5.0") {
    exclude(group = "org.jetbrains.kotlin")
  }
  if (System.getenv("NO_KT") == "false") {
    pkgIn("org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
    pkgIn("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0")
  }
  pkgIn("org.jetbrains.kotlin:kotlin-reflect:1.7.0") {
    isTransitive = false
  }
  pkgIn("com.github.jknack:handlebars:4.3.0")
  pkgIn("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.3")
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
      languageVersion = "1.5"
      freeCompilerArgs = listOf("-Xinline-classes", "-Xopt-in=kotlin.RequiresOptIn")
    }
  }
}
