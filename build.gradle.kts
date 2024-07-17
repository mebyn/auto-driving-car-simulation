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
  implementation("com.michael-bull.kotlin-result:kotlin-result:2.0.0")

  testImplementation(kotlin("test"))
  testImplementation("org.assertj:assertj-core:3.25.1")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(21)
}

tasks {
  ktlintCheck {
    dependsOn += ktlintFormat
  }
}
