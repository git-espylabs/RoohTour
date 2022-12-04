plugins {
    `kotlin-dsl`
}

repositories {
    // The org.jetbrains.kotlin.jvm plugin requires a repository
    // where to download the Kotlin compiler dependencies from.
    jcenter()
    google()
}

object PluginsVersions {
    const val GRADLE = "4.2.1"
    const val KOTLIN = "1.5.20"
}

dependencies {
    implementation("com.android.tools.build:gradle:${PluginsVersions.GRADLE}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginsVersions.KOTLIN}")
    implementation("org.jetbrains.kotlin:kotlin-serialization:${PluginsVersions.KOTLIN}")
}