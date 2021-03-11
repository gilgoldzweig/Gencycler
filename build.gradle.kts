plugins {
    kotlin("jvm") version "1.4.30" apply false
    id("com.android.application") version "7.0.0-alpha07" apply false
    id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha02" apply false
    id("io.gitlab.arturbosch.detekt").version("1.16.0")
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
        jcenter()
    }
//    if (has) {
//
//    }
}
repositories {
    jcenter()
}

detekt {
    toolVersion = "1.16.0"
    config = files("config/detekt/detekt.yml")

    reports {
        html {
            enabled = true
            destination = file("reports/detekt/detekt.html")
        }
    }
}

