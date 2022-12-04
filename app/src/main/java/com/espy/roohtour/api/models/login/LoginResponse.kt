package com.espy.roohtour.api.models.login

import com.espy.roohtour.api.models.ResponseBase
import kotlinx.serialization.SerialName

class LoginResponse(
    @SerialName("data")
    var data: String? = ""
) : ResponseBase()