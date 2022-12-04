package com.espy.roohtour.api.models.common

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class ExpenseTypes(
    @SerialName("data")
    var data: List<ExpenseType> = listOf()
) : ResponseBase()
