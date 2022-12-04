// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {

    val kotlin_version by extra("1.5.20")
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }

    dependencies {
        classpath (Plugins.ANDROID_GRADLE_PLUGIN)
        classpath(Plugins.KOTLIN_GRADLE_PLUGIN)
        classpath(Plugins.GRADLE_VERSIONS)
//        classpath(Plugins.GOOGLE_SERVICE)
        classpath(Plugins.NAVIGATION_SFE_ARGS)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

apply(plugin = "com.github.ben-manes.versions")

configurations {
    register("bom")
    register("upToDate")
    register("exceedLatest")
    register("platform")
    register("upgradesFound")
    register("upgradesFound2")
    register("unresolvable")
    register("unresolvable2")
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {

    // Example 1: reject all non stable versions
    rejectVersionIf {
        candidate.version.isNonStable()
    }

    // Example 2: disallow release candidates as upgradable versions from stable versions
    rejectVersionIf {
        candidate.version.isNonStable() && !currentVersion.isNonStable()
    }

    // Example 3: using the full syntax
    resolutionStrategy {
        componentSelection {
            all {
                if (candidate.version.isNonStable() && !currentVersion.isNonStable()) {
                    reject("Release candidate")
                }
            }
        }
    }

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}