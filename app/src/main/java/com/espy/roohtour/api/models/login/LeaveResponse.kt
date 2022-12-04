package com.espy.roohtour.api.models.login

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class LeaveResponse(
    @SerialName("data")
    var data: String? = ""
) : ResponseBase()