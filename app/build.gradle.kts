plugins {
    id("gencycler.android.application")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.gillongname.gencycler"
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.0.0-Beta1"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    implementation (libs.androidx.annotation)
    implementation (libs.androidx.recyclerview)

    implementation(project(":core"))
    ksp(project(":processor"))
}
