plugins {
    id("com.android.library")
}

android {
    namespace = "com.bzlzhh.ng_gl4es"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    lint {
        targetSdk = libs.versions.targetSdk.get().toInt()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            initWith(getByName("debug"))
        }
    }

    externalNativeBuild {
        cmake {
            path = file("NG-GL4ES/CMakeLists.txt")
        }
    }

    ndkVersion = "27.0.12077973"
}

tasks.register("buildNGG") {
    dependsOn("assemble")
}
