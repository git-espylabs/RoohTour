package com.espy.roohtour.api.models.shops

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class DeliveryShops(
    @SerialName("data")
    var data: List<DeliveryShop>
) : ResponseBase()
