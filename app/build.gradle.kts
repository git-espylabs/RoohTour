plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    compileSdkVersion(BuildConfig.COMPILE_SDK_VERSION)
    buildToolsVersion(BuildConfig.BUILD_TOOL_VERSION)

    defaultConfig {
        applicationId = BuildConfig.APPLICATION_ID

        minSdkVersion(BuildConfig.MIN_SDK_VERSION)
        targetSdkVersion(BuildConfig.TARGET_SDK_VERSION)

        versionCode = BuildConfig.VERSION_CODE
        versionName = BuildConfig.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lintOptions {
        isCheckReleaseBuilds =  false
        isAbortOnError = false
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    flavorDimensions(BuildProductDimensions.Environment)
    productFlavors {

        create(ProductFlavors.Staging) {
            dimension = BuildProductDimensions.Environment
            versionNameSuffix = ".stage"
        }

        create(ProductFlavors.Production) {
            dimension = BuildProductDimensions.Environment
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    /** Build features */
    android.buildFeatures.dataBinding = true
    android.buildFeatures.viewBinding = true

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.KOTLIN_REFLECT)
    implementation(Dependencies.KOTLIN_COROUTINE_CORE)
    implementation(Dependencies.KOTLIN_COROUTINE_ANDROID)
    implementation(Dependencies.ANDROIDX_CORE_KTX)
    implementation(Dependencies.ANDROIDX_APP_COMPAT)
    implementation(Dependencies.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(Dependencies.VIEW_MODEL_LIFE_CYCLE)
    implementation(Dependencies.ANDROIDX_LIFE_CYCLE_KTX)
    implementation(Dependencies.LIFECYCLE_RUNTIME)
    implementation(Dependencies.LIFECYCLE_COMMON)
    implementation (Dependencies.ANDROIDX_ACTIVITY)
    implementation (Dependencies.ANDROIDX_FRAGMENT)
    implementation(Dependencies.ANDROID_DESIGN)
    implementation(Dependencies.FRAGMENT)
    implementation(Dependencies.NAVIGATION)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.LOCATION)
    implementation(Dependencies.KOTLIN_SERIALIZATION)
    implementation(Dependencies.ANDROIDX_PREFERENCE)
    implementation(Dependencies.APACHE_COMMONS_COLLECTIONS)
    implementation(Dependencies.GLIDE)
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("org.apache.commons:commons-io:1.3.2")
    implementation(Dependencies.COUNTABLE_FAB)

    // room
    kapt(Dependencies.ANDROIDX_ROOM_COMPILER)
    implementation(Dependencies.ANDROIDX_ROOM)
    implementation(Dependencies.ANDROIDX_ROOM_KTX)

    // OkHttpClient
    implementation(Dependencies.OK_HTTP3)
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.RETROFIT_CONVERTER)
    implementation(Dependencies.COROUTINE_ADAPTER)

    testImplementation(Dependencies.JUNIT)
    androidTestImplementation(Dependencies.ANDROIDX_JUNIT)
    androidTestImplementation(Dependencies.ANDROIDX_ESPRESSO)

    //Coroutines
    implementation(Dependencies.COROUTINE_ANDROID)
}