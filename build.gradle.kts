plugins {
    kotlin("jvm") version "1.4.30" apply false
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    val kotlin_version by extra("1.4.30")
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath(kotlin("gradle-plugin", version = kotlin_version))
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}