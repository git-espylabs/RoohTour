package com.espy.roohtour.api.models.shops

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class FeedbackResponse(
    @SerialName("data")
    var data: String? = ""
) : ResponseBase()
