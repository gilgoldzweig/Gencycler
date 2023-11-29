import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}


gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "gencycler.android.application"
            implementationClass = "com.gillongname.gencycler.plugins.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "gencycler.android.library"
            implementationClass = "com.gillongname.gencycler.plugins.AndroidLibraryConventionPlugin"
        }
        register("jvmLibrary") {
            id = "gencycler.jvm.library"
            implementationClass = "com.gillongname.gencycler.plugins.JvmLibraryConventionPlugin"
        }
    }
}