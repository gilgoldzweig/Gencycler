import com.android.sdklib.AndroidVersion.VersionCodes
plugins {
    id("com.android.application")
    id("kotlin-ksp") version "1.4.0-rc-dev-experimental-20200828"
    kotlin("android")
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
    ksp(project(":processor"))
}