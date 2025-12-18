plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.androidLint) apply false
}

val deployDir = layout.buildDirectory.dir("deploy")

// Custom Copy task that logs the destination after copying
abstract class LoggingCopy : Copy() {
    @get:Input
    abstract val logMessage: Property<String>

    @TaskAction
    fun logAfterCopy() {
        logger.lifecycle(logMessage.get())
    }
}

// Custom task for aggregating deployments with summary logging
abstract class DeploymentSummary : DefaultTask() {
    @get:Input
    abstract val summaryMessage: Property<String>

    @TaskAction
    fun printSummary() {
        logger.lifecycle(summaryMessage.get())
    }
}

tasks.register<LoggingCopy>("deployBackend") {
    group = "deployment"
    description = "Builds and copies backend JAR to deploy directory"
    dependsOn(":backend:prepareBackendDeploy")
    from(project(":backend").layout.buildDirectory.dir("deploy"))
    into(deployDir.map { it.dir("backend") })
    logMessage.set(destinationDir.absolutePath.let { "Backend deployed to: $it" })
}

tasks.register<LoggingCopy>("deployWebJs") {
    group = "deployment"
    description = "Builds and copies JS web app to deploy directory"
    dependsOn(":composeApp:prepareJsWebDeploy")
    from(project(":composeApp").layout.buildDirectory.dir("deploy/js"))
    into(deployDir.map { it.dir("web/js") })
    logMessage.set(destinationDir.absolutePath.let { "JS web app deployed to: $it" })
}

tasks.register<LoggingCopy>("deployWebWasm") {
    group = "deployment"
    description = "Builds and copies WASM web app to deploy directory"
    dependsOn(":composeApp:prepareWasmWebDeploy")
    from(project(":composeApp").layout.buildDirectory.dir("deploy/wasm"))
    into(deployDir.map { it.dir("web/wasm") })
    logMessage.set(destinationDir.absolutePath.let { "WASM web app deployed to: $it" })
}

tasks.register<DeploymentSummary>("deployWeb") {
    group = "deployment"
    description = "Builds and copies both JS and WASM web apps to deploy directory"
    dependsOn("deployWebJs", "deployWebWasm")
    summaryMessage.set("All web apps deployed to: ${deployDir.get().asFile.absolutePath}/web/")
}

tasks.register<DeploymentSummary>("deployAll") {
    group = "deployment"
    description = "Builds and copies backend and all web apps to deploy directory"
    dependsOn("deployBackend", "deployWeb")
    val deployPath = deployDir.get().asFile.absolutePath
    summaryMessage.set("""
        |
        |=== Deployment Complete ===
        |All artifacts ready at: $deployPath/
        |  - Backend JAR: backend/mysterybox-backend.jar
        |  - JS Web App:  web/js/
        |  - WASM Web App: web/wasm/
    """.trimMargin())
}