package com.espy.roohtour.api.models.products

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class Categories(
    @SerialName("data")
    var data: List<Category> = listOf()
) : ResponseBase()
