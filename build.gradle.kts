import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
  application
  kotlin("jvm") version "2.0.0"
  kotlin("plugin.power-assert") version "2.0.0"
  id("com.adarshr.test-logger") version "4.0.0"
  id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "com.zuhlke"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  testImplementation("io.mockk:mockk:1.13.12")
  testImplementation("org.assertj:assertj-core:3.25.1")
}

application {
  mainClass = "com.zuhlke.AutoDrivingCarSimulatorKt"
}

kotlin {
  jvmToolchain(21)
}

tasks {
  test {
    useJUnitPlatform()
    reports.html.required = true
    reports.html.outputLocation = layout.projectDirectory.dir("test-report")
  }
  ktlintCheck {
    dependsOn += ktlintFormat
  }
  named<JavaExec>("run") {
    standardInput = System.`in`
  }
}

testlogger {
  theme = ThemeType.MOCHA
}
