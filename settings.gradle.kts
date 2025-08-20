pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "FCL"
include(":FCL")
include(":FCLCore")
include(":FCLauncher")
include(":FCLLibrary")
include(":LWJGL-Pojav")
include(":LWJGL-Boat")
include(":NG-GL4ES")
include(":ZipFileSystem")
