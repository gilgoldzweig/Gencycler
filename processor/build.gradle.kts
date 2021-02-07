plugins {
    kotlin("jvm")
}

group = Config.groupId
version = "3.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.4.30-dev-experimental-20210205")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

