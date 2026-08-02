import com.android.build.api.variant.FilterConfiguration.FilterType.ABI
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.android.build.gradle.tasks.MergeSourceSetFolders
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.20"
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
        versionCode = 1321
        versionName = "1.3.2.1"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
        resValues = true
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

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                (output.getFilter(ABI)?.identifier ?: "all").let { abi ->
                    output.outputFileName =
                        "FCL-${variant.buildType}-${project.android.defaultConfig.versionName}-${abi}.apk"
                }

                val variantName = variant.name.replaceFirstChar { it.uppercaseChar() }
                afterEvaluate {
                    val task =
                        tasks.named("merge${variantName}Assets").get() as MergeSourceSetFolders
                    task.doLast {
                        val arch = System.getProperty("arch", "all")
                        val assetsDir = task.outputDir.get().asFile
                        copyAssetsFile(File("${project.projectDir}/src/main/assets"), assetsDir)
                        val jreList = listOf("jre8", "jre17", "jre21", "jre25")
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

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(project(":FCLCore"))
    implementation(project(":FCLLibrary"))
    implementation(project(":FCLauncher"))
    implementation(project(":Terracotta"))
    implementation(libs.taptargetview)
    implementation(libs.nanohttpd)
    implementation(libs.commons.compress)
    implementation(libs.xz)
    implementation(libs.opennbt)
    implementation(libs.gson)
    implementation(libs.appcompat)
    implementation(libs.core.splashscreen)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.glide)
    implementation(libs.touchcontroller)
    implementation(libs.palette.ktx)
    implementation(libs.gamepad.remapper)
    implementation(libs.segmented.button)
    implementation(libs.datastore)
    implementation(libs.kotlinx.serialization.json)
}
