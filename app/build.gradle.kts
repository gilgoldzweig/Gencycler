plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
}

android {
    buildToolsVersion = "30.0.3"
    compileSdkVersion(30)

    defaultConfig {
        applicationId = Config.groupId
        versionCode = Config.versionCode
        versionName = Config.versionName
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
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

    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    implementation(project(":core"))
//    implementation(project(":processor"))
    ksp(project(":processor"))
}
