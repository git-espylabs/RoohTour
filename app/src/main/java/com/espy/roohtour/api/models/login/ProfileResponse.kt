package com.espy.roohtour.api.models.login

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

data class ProfileResponse(
    @SerialName("data")
    var data: List<Profile> = listOf()
) : ResponseBase()
