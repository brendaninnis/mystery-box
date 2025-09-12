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
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.7")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.7")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.7")
    implementation("io.ktor:ktor-server-status-pages:2.3.7")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.7")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.7")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.46.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.46.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.46.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.46.0")
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("com.zaxxer:HikariCP:5.0.1")

    // Database migrations
    implementation("org.flywaydb:flyway-core:9.22.3")

    // Password hashing
    implementation("at.favre.lib:bcrypt:0.10.2")

    // JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Test dependencies
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.21")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
}

application {
    mainClass.set("ca.realitywargames.mysterybox.backend.ApplicationKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ca.realitywargames.mysterybox.backend.ApplicationKt"
    }
}
