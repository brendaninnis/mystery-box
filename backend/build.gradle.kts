plugins {
    alias(libs.plugins.kotlinJvm)
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ca.realitywargames.mysterybox"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))

    // Ktor server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.serialization.kotlinx.json)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)

    // Database
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.postgresql)
    implementation(libs.hikaricp)

    // Database migrations
    implementation(libs.flyway.core)

    // Password hashing
    implementation(libs.bcrypt)

    // JSON
    implementation(libs.kotlinx.serialization.json.jvm)

    // Logging
    implementation(libs.logback.classic)

    // Test dependencies
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit.jupiter)
}

application {
    mainClass.set("ca.realitywargames.mysterybox.backend.ApplicationKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ca.realitywargames.mysterybox.backend.ApplicationKt"
    }
}
