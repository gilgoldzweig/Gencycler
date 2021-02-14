include(":app")
include(":processor")
include(":core")
include(":annotations")
pluginManagement {

//    resolutionStrategy {
//        eachPlugin {
//            when (requested.id.id) {
//                "kotlin-ksp",
//                "org.jetbrains.kotlin.kotlin-ksp",
//                "org.jetbrains.kotlin.ksp" ->
//                    useModule("org.jetbrains.kotlin:kotlin-ksp:${requested.version}")
//            }
//        }
//    }

    repositories {
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        google()
    }
}

rootProject.name = "GencyclerV3"
