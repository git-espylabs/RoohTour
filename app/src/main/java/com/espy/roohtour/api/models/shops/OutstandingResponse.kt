package com.espy.roohtour.api.models.shops

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class OutstandingResponse (
    @SerialName("data")
    var data: List<ShopOutstanding> = listOf()
) : ResponseBase()