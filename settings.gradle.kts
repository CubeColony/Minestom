enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
        maven("https://maven.cubecolony.net/repository/maven-releases/")
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
include("demo")