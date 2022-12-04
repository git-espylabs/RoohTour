package com.espy.roohtour.api.models.order

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

class OrderResponse(
    @SerialName("data")
    var data: String? = ""
) : ResponseBase()