plugins {
  kotlin("jvm") version "2.0.0"
  id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "com.zuhlke"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(21)
}
