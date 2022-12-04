
object BuildConfig {

    const val APPLICATION_ID = "com.espy.roohtour"
    const val VERSION_CODE = 6
    const val VERSION_NAME = "1.5"

    const val COMPILE_SDK_VERSION = 30
    const val TARGET_SDK_VERSION = 30
    const val MIN_SDK_VERSION = 23
    const val BUILD_TOOL_VERSION = "30.0.3"
}

object Versions {
    const val GRADLE = "4.2.1"
    const val KOTLIN = "1.5.20"
    const val KOTLIN_CORE = "1.5.0"
    const val ANDROIDX_ACTIVITY = "1.2.3"
    const val ANDROIDX_FRAGMENT = "1.3.4"
    const val KOTLIN_SERIALIZATION = "1.2.1"
    const val KOIN = "2.2.2"
    const val COIL = "1.2.1"
    const val KTX = "1.3.2"
    const val ROOM = "2.3.0"
    const val PREFERENCE = "1.1.1"
    const val APP_COMPAT = "1.3.0"
    const val CONSTRAINT_LAYOUT = "2.0.4"
    const val COROUTINE_ANDROID = "1.3.5"
    const val OK_HTTP_LOGGING = "4.9.1"
    const val RETROFIT = "2.9.0"
    const val COROUTINES_ADAPTER = "0.9.2"
    const val ESPRESSO = "3.3.0"
    const val ANDROIDX_UNIT_TEST = "1.1.2"
    const val DESIGN = "28.0.0"
    const val KTX_LIFE_CYCLE = "2.3.1"
    const val NAVIGATION = "2.3.5"
    const val VIEW_PAGER = "1.0.0"
    const val FRAGMENT = "2.3.0"
    const val MATERIAL = "1.3.0"
    const val LOCATION = "18.0.0"
    const val GOOGLE_SERVICE = "4.3.8"
    const val MANES_GRADLE_VERSION = "0.36.0"
    const val GOOGLE_GSON = "2.8.6"
    const val GLIDE_VERSION = "4.9.0"
}

object Dependencies{
    const val VIEW_MODEL_LIFE_CYCLE =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.KTX_LIFE_CYCLE}"
    const val ANDROIDX_LIFE_CYCLE_KTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.KTX_LIFE_CYCLE}"
    const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime:${Versions.KTX_LIFE_CYCLE}"
    const val LIFECYCLE_COMMON =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.KTX_LIFE_CYCLE}"
    const val ANDROIDX_CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val ANDROIDX_APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val ANDROID_DESIGN = "com.android.support:design:${Versions.DESIGN}"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    const val KOTLIN_COROUTINE_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_CORE}"
    const val KOTLIN_COROUTINE_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLIN_CORE}"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
    const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_SERIALIZATION}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Versions.KTX}"
    const val ANDROIDX_PREFERENCE = "androidx.preference:preference-ktx:${Versions.PREFERENCE}"
    const val ANDROIDX_ROOM = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ANDROIDX_ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ANDROIDX_ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val NAVIGATION = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val ANDROIDX_ACTIVITY = "androidx.activity:activity:${Versions.ANDROIDX_ACTIVITY}"
    const val ANDROIDX_FRAGMENT = "androidx.fragment:fragment:${Versions.ANDROIDX_FRAGMENT}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val COROUTINE_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE_ANDROID}"
    const val LOCATION = "com.google.android.gms:play-services-location:${Versions.LOCATION}"

    //3rd party libraries
    const val OK_HTTP3 = "com.squareup.okhttp3:logging-interceptor:${Versions.OK_HTTP_LOGGING}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val COROUTINE_ADAPTER =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.COROUTINES_ADAPTER}"
    const val KOIN_VIEWMODEL = "org.koin:koin-android-viewmodel:${Versions.KOIN}"
    const val KOIN = "org.koin:koin-android:${Versions.KOIN}"
    const val COIL = "io.coil-kt:coil:${Versions.COIL}"
    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE_VERSION}"

    const val APACHE_COMMONS_COLLECTIONS = "org.apache.commons:commons-collections4:4.4"
    const val COUNTABLE_FAB = "com.github.andremion:counterfab:1.2.2"

    //Test libraries
    const val ANDROIDX_ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    const val ANDROIDX_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_UNIT_TEST}"
    const val JUNIT = "junit:junit:4.+"
}

object Plugins {
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val ANDROID_GRADLE_PLUGIN = "com.android.tools.build:gradle:${Versions.GRADLE}"
    const val GOOGLE_SERVICE = "com.google.gms:google-services:${Versions.GOOGLE_SERVICE}"
    const val GRADLE_VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${Versions.MANES_GRADLE_VERSION}"
    const val NAVIGATION_SFE_ARGS = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"

    /** Subsystem */
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"
    const val ANDROID_EXTENSIONS = "android.extensions"
    const val KOTLIN_ANDROID = "kotlin-android"
    const val KOTLIN_KAPT = "kapt"
    const val KOTLIN_PARCELIZE = "kotlin-parcelize"
    const val KOTLIN_SERIALIZATION = "kotlinx-serialization"
}

