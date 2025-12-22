plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
}

group = "org.laggyrocket"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Ktor Client (using bundle)
    implementation(libs.bundles.ktor.client)

    // Kotlinx Libraries (using bundle)
    implementation(libs.bundles.kotlinx)

    // Logging
    implementation(libs.slf4j.simple)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.laggyrocket.MainKt")
}