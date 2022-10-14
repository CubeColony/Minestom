import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("minestom.common-conventions")
    id("minestom.native-conventions")
    id("com.github.johnrengelman.shadow") version ("7.1.1")
}

application {
    mainClass.set("com.cubecolony.server.ServerLauncher")
    // This is included because Shadow is buggy. Wait for https://github.com/johnrengelman/shadow/issues/613 to befixed.
    @Suppress("DEPRECATION")
    mainClassName = "com.cubecolony.server.ServerLauncher"
}

dependencies {
    implementation(rootProject)
}

tasks.withType<ShadowJar> {
    archiveFileName.set("cubestom.jar")
}