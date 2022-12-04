package extensions

import com.android.build.gradle.internal.dsl.DefaultConfig

fun DefaultConfig.buildConfigStringField(name: String, value: String) {
    this.buildConfigField("String", name, "\"$value\"")
}