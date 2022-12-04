package com.espy.roohtour.api.models.common

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class PaymentTypes(
    @SerialName("data")
    var data: List<PaymentType> = listOf()
) : ResponseBase()
