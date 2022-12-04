package com.espy.roohtour.api.models.products

import com.espy.roohtour.api.models.ResponseBase
import com.espy.roohtour.ui.products.models.TodayMyOrder
import kotlinx.serialization.SerialName

data class TodayMyOrdersResp(
    @SerialName("data")
    var data: List<TodayMyOrder>
) : ResponseBase()
