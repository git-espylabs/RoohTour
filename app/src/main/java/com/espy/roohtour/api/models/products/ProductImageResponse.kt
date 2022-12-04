package com.espy.roohtour.api.models.products

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class ProductImageResponse(
    @SerialName("data")
    var data: List<ProductImage> = listOf()
) : ResponseBase()
