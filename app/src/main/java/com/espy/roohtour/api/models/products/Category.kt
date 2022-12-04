package com.espy.roohtour.api.models.products

import kotlinx.serialization.SerialName

data class Category(
    @SerialName("id")
    var id: String? = "",
    @SerialName("parent_category")
    var parent_category: String? = "",
    @SerialName("category_name")
    var category_name: String? = "",
    @SerialName("created_at")
    var created_at: String? = "",
    @SerialName("updated_at")
    var updated_at: String? = ""
)
