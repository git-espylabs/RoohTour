package extensions

import com.android.build.gradle.internal.dsl.ProductFlavor

/**
 * Extension to adds a new string field to the generated BuildConfig class for a flavour.
 *
 * @param name the name of the field
 * @param value the string value of the field
 */
fun ProductFlavor.buildConfigStringField(name: String, value: String) {
    this.buildConfigField("String", name, "\"$value\"")
}