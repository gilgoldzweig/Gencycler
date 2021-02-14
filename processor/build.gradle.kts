plugins {
    kotlin("jvm")
}

group = Config.groupId
version = "3.0-SNAPSHOT"


dependencies {
    implementation(kotlin("stdlib"))
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    implementation(project(":annotations"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.4.30-1.0.0-alpha02")
    implementation("com.github.tschuchortdev:kotlin-compile-testing:1.3.5")
    implementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.3.5")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

