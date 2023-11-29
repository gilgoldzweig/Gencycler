//package com.gillongname.gencycler.plugins
//
//import Config
//import com.android.build.gradle.BaseExtension
//import com.gillongname.gencycler.extensions.implementation
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//import org.gradle.kotlin.dsl.dependencies
//import org.gradle.kotlin.dsl.findByType
//
//class AndroidModulePlugin : Plugin<Project> {
//    private val Project.android: BaseExtension
//        get() = extensions.findByType() ?: error("Not an Android module: $name")
//
//    override fun apply(target: Project) {
//        with(target) {
//            androidConfig()
//            dependenciesConfig()
//        }
//    }
//
//
//    private fun Project.androidConfig() {
//        android.run {
//            namespace = "${Config.groupId}.$name"
//            compileSdkVersion(Config.compileSdk)
//            defaultConfig {
//                minSdk = Config.minSdk
//                targetSdk = Config.targetSdk
//                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//                consumerProguardFiles("consumer-rules.pro")
//            }
//            buildTypes {
//                getByName("release") {
//                    isMinifyEnabled = false
//                    proguardFiles(
//                        getDefaultProguardFile("proguard-android-optimize.txt"),
//                        "proguard-rules.pro"
//                    )
//                    proguardFiles.add(file("proguard-rules.txt"))
//                }
//
//                getByName("debug") {
//                    isMinifyEnabled = false
//                }
//            }
//        }
//    }
//
//    private fun Project.dependenciesConfig() {
//        dependencies {
//            implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
//        }
//    }
//}