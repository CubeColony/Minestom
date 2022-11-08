enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")


dependencyResolutionManagement {
    val nexusUsername: String by settings
    val nexusPassword: String by settings

    repositories {
        maven("https://jitpack.io")
        maven {
            url = uri("https://maven.cubecolony.net/repository/maven-releases/")
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
        maven {
            url = uri("https://maven.cubecolony.net/repository/maven-snapshots/")
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}

rootProject.name = "Cubestom"
include("code-generators")
include("jmh-benchmarks")
include("jcstress-tests")
include("server")
include("demo")
include("testing")