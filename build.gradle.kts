plugins {
    kotlin("jvm") version "1.4.20"
}

buildscript {
    val kotlin_version by extra("1.4.21")
    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlin_version))
        classpath("com.android.tools.build:gradle:4.0.2")
    }
}

repositories {
    mavenCentral()
    google()
}