plugins {
    id("gencycler.jvm.library")
}

version = "3.0-SNAPSHOT"


dependencies {
    implementation(kotlin("stdlib"))
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.0.0-Beta1"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    implementation(project(":annotations"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.21-1.0.15")
    implementation("com.github.tschuchortdev:kotlin-compile-testing:1.3.5")
    implementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.3.5")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

