import org.gradle.api.JavaVersion

object Config {
    const val groupId = "com.gilongname.gencycler"
    const val minSdk = 21
    const val compileSdk = "30"
    const val targetSdk = 30
    val javaVersion = JavaVersion.VERSION_1_8
    const val buildTools = "30.0.3"

    const val versionCode = 1
    const val versionName = "1.0.0"
}
