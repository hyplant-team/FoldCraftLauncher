import com.android.build.api.variant.FilterConfiguration.FilterType.ABI
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.android.build.gradle.tasks.MergeSourceSetFolders
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun copyAssetsFile(source: File, target: File) {
    if (source.isDirectory) {
        if (!target.exists()) {
            target.mkdirs()
        }
        source.listFiles()?.forEach { file ->
            val targetFile = File(target, file.name)
            copyAssetsFile(file, targetFile)
        }
    } else {
        Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tungsten.fcl"
    compileSdk = libs.versions.compileSdk.get().toInt()

    var pkgName = System.getProperty("pkgName", "com.tungsten.fcl")
    if (pkgName.isEmpty()) {
        pkgName = "com.tungsten.fcl"
    }
    var appName = System.getProperty("appName", "Fold Craft Launcher")
    if (appName.isEmpty()) {
        appName = "Fold Craft Launcher"
    }

    signingConfigs {
        create("FCLKey") {
            storeFile = file("key-fcl-debug.jks")
            storePassword = "FCL-Debug"
            keyAlias = "FCL-Debug"
            keyPassword = "FCL-Debug"
        }
        create("FCLDebugKey") {
            storeFile = file("key-android-test.jks")
            storePassword = "keystore-pass"
            keyAlias = "testkey"
            keyPassword = "keystore-pass"
        }
    }

    defaultConfig {
        applicationId = pkgName
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1247
        versionName = "1.2.4.7"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("FCLKey")
        }
        getByName("debug") {
            initWith(getByName("debug"))
            signingConfig = signingConfigs.getByName("FCLDebugKey")
        }
        configureEach {
            resValue("string", "app_name", appName)
            resValue("string", "app_version", android.defaultConfig.versionName.toString())
        }
    }

    androidComponents {
        onVariants { variant ->
            variant.outputs.forEach { output ->
                if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                    (output.getFilter(ABI)?.identifier ?: "all").let { abi ->
                        output.outputFileName =
                            "FCL-${variant.buildType}-${defaultConfig.versionName}-${abi}.apk"
                    }

                    val variantName = variant.name.replaceFirstChar { it.uppercaseChar() }
                    afterEvaluate {
                        val task =
                            tasks.named("merge${variantName}Assets").get() as MergeSourceSetFolders
                        task.doLast {
                            val arch = System.getProperty("arch", "all")
                            val assetsDir = task.outputDir.get().asFile
                            copyAssetsFile(File("${project.projectDir}/src/main/assets"), assetsDir)
                            val jreList = listOf("jre8", "jre11", "jre17", "jre21")
                            println("arch:$arch")
                            jreList.forEach { jre ->
                                val runtimeDir = "$assetsDir/app_runtime/java/$jre"
                                println("runtimeDir:$runtimeDir")
                                File(runtimeDir).listFiles().forEach {
                                    if (arch != "all" && it.name != "version" && !it.name.contains("universal") && it.name != "bin-${arch}.tar.xz") {
                                        println("delete:${it} : ${it.delete()}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
            pickFirsts += listOf("**/libbytehook.so")
        }
    }

    androidResources{
        ignoreAssetsPattern = "!.svn:!.git:!.ds_store:!*.scc:!.*:!CVS:!thumbs.db:!picasa.ini:!*~"
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    splits {
        val arch = System.getProperty("arch", "all")
        if (arch != "all") {
            abi {
                isEnable = true
                reset()
                when (arch) {
                    "arm" -> include("armeabi-v7a")
                    "arm64" -> include("arm64-v8a")
                    "x86" -> include("x86")
                    "x86_64" -> include("x86_64")
                }
            }
        }
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":FCLCore"))
    implementation(project(":FCLLibrary"))
    implementation(project(":FCLauncher"))
    implementation(project(":NG-GL4ES"))
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.14.0")
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("org.tukaani:xz:1.9")
    implementation("com.github.steveice10:opennbt:1.5")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("top.fifthlight.touchcontroller:proxy-client-android:0.0.5")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("com.github.Mathias-Boulay:android_gamepad_remapper:2.0.3")
    implementation("com.github.addisonElliott:SegmentedButton:3.1.9")
}
