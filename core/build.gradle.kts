plugins {
    id("com.android.library")
    id("io.gitlab.arturbosch.detekt")
    kotlin("android")
}

android {
    buildToolsVersion = "30.0.3"
    compileSdkVersion(30)

    defaultConfig {
        versionCode = Config.versionCode
        versionName = Config.versionName
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}


repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation ("androidx.annotation:annotation:1.1.0")
    implementation ("androidx.recyclerview:recyclerview:1.1.0")
    api(project(":annotations"))

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("io.mockk:mockk:1.10.6")

    val kotestVersion = "4.4.3"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
}

