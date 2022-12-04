package com.espy.roohtour.api.models.shops

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class FollowupResponse(
    @SerialName("data")
    var data: Int? = 0
) : ResponseBase()