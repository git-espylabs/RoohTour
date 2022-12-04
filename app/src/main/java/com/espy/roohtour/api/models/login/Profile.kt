package com.espy.roohtour.api.models.login

import kotlinx.serialization.SerialName

data class Profile(
    @SerialName("login_id")
    var login_id: String? = "",
    @SerialName("staff_name")
    var staff_name: String? = "",
    @SerialName("mobile_number")
    var mobile_number: String? = "",
    @SerialName("email")
    var email: String? = "",
    @SerialName("address")
    var address: String? = "",
    @SerialName("image")
    var image: String? = "",
)
