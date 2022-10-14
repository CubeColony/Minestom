plugins {
    `java-library`
    id("minestom.native-conventions")
    `maven-publish`
    id("io.ebean") version "13.9.4"
}

allprojects {
    group = "net.minestom.server"
    version = "1.2"
    description = "Lightweight and multi-threaded Minecraft server implementation"
}

sourceSets {
    main {
        java {
            srcDir(file("src/autogenerated/java"))
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks {
    withType<Javadoc> {
        (options as? StandardJavadocDocletOptions)?.apply {
            encoding = "UTF-8"

            // Custom options
            addBooleanOption("html5", true)
            addStringOption("-release", "17")
            // Links to external javadocs
            links("https://docs.oracle.com/en/java/javase/17/docs/api/")
            links("https://jd.adventure.kyori.net/api/${libs.versions.adventure.get()}/")
        }
    }
    withType<Zip> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

dependencies {
    // Junit Testing Framework
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.suite.api)
    testRuntimeOnly(libs.junit.suite.engine)
    // Only here to ensure J9 module support for extensions and our classloaders
    testCompileOnly(libs.mockito.core)


    // Logging
    implementation(libs.bundles.logging)
    // Libraries required for the terminal
    implementation(libs.bundles.terminal)

    // Performance improving libraries
    implementation(libs.caffeine)
    api(libs.fastutil)
    implementation(libs.bundles.flare)

    // Libraries
    api(libs.gson)
    implementation(libs.jcTools)
    // Path finding
    api(libs.hydrazine)

    // Adventure, for user-interface
    api(libs.bundles.adventure)

    // Kotlin Libraries
    api(libs.bundles.kotlin)

    // Extension Management System dependency handler.
    api(libs.dependencyGetter)

    // CubeColony
    implementation(libs.bundles.cubeColony)

    // Ebean
    implementation(libs.bundles.ebean)
    annotationProcessor(libs.ebean.query)
    testImplementation(libs.ebean.test)
    testImplementation(libs.ebean.sqlite)
    testImplementation("org.xerial:sqlite-jdbc:3.39.3.0")

    // Minestom Data (From MinestomDataGenerator)
    implementation(libs.minestomData)

    // NBT parsing/manipulation/saving
    api("io.github.jglrxavpok.hephaistos:common:${libs.versions.hephaistos.get()}")
    api("io.github.jglrxavpok.hephaistos:gson:${libs.versions.hephaistos.get()}")
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                group = "com.cubecolony"
                artifactId = "cubestom"
                version = "1.2"
            }

            from(components["java"])
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = uri("https://maven.cubecolony.net/repository/maven-releases/")
            val snapshotsRepoUrl = uri("https://maven.cubecolony.net/repository/maven-snapshots/")

            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = project.properties["nexusUsername"] as String
                password = project.properties["nexusPassword"] as String
            }
        }
    }
}

