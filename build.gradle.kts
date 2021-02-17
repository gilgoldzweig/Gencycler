plugins {
    kotlin("jvm") version "1.4.30" apply false
    id("com.android.application") version "4.2.0-beta02" apply false
    id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha02" apply false
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    val kotlin_version by extra("1.4.30")
    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlin_version))
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}