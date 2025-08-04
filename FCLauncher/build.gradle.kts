plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    // var pkgSuffix = System.getProperty("pkgSuffix", "modpack")
    // if (pkgSuffix.isEmpty()) {
    //     pkgSuffix = "modpack"
    // }

    namespace = "com.tungsten.fclauncher"
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
        configureEach {
            // resValue("string", "file_browser_provider", "com.tungsten.fcl." + pkgSuffix + ".provider")
            // resValue("string", "file_browser_document_provider", "com.tungsten.fcl." + pkgSuffix + ".document.provider")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    externalNativeBuild {
        ndkBuild {
            path = file("src/main/jni/Android.mk")
        }
    }

    ndkVersion = "27.0.12077973"

    buildFeatures {
        prefab = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("com.bytedance:bytehook:1.0.10")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}