package com.espy.roohtour.api.models.shops

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class OrderItemResponse(
    @SerialName("data")
var data: List<OrderItem>?
) : ResponseBase()
