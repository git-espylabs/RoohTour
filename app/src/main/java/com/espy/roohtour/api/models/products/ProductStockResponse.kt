package com.espy.roohtour.api.models.products

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class ProductStockResponse(
    @SerialName("data")
    var data: List<ProductStock> = listOf()
) : ResponseBase()