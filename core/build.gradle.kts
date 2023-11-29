
plugins {
    id("gencycler.android.library")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.0.0-Beta1"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)

    api(project(":annotations"))
}