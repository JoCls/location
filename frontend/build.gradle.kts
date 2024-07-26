plugins {
    id("base") // Base plugin is often used for common tasks
}

tasks.register<Exec>("installFrontendDependencies") {
    workingDir = file(project.projectDir)
    commandLine("C:\\Program Files\\nodejs\\npm.cmd", "install")
}

tasks.register<Exec>("buildFrontend") {
    dependsOn("installFrontendDependencies")
    workingDir = file(project.projectDir)
    commandLine("C:\\Program Files\\nodejs\\npm.cmd", "run", "build")
}

tasks.register<Copy>("copyFrontendBuild") {
    dependsOn("buildFrontend")
    from(file("${project.projectDir}/build"))
    into(file("${rootProject.projectDir}/backend/src/main/resources/static")) // Adjust as needed
}

tasks.register<Delete>("o") {
    delete(file("${project.projectDir}/build"))
}

// Ensure buildFrontend runs before the copyFrontendBuild task
tasks.named("build") {
    dependsOn("buildFrontend")
}