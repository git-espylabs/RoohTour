package com.espy.roohtour.api.models.products

import com.google.gson.annotations.SerializedName

data class ProductsByCategoryRequest(
    @SerializedName("categoryid")
    var categoryid: String = ""
)
