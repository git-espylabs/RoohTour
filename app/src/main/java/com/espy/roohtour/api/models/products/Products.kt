package com.espy.roohtour.api.models.products

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class Products(
    @SerialName("data")
    var data: List<Product> = listOf()
) : ResponseBase()
