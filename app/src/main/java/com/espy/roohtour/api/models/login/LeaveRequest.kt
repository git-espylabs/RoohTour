package com.espy.roohtour.api.models.login

import com.google.gson.annotations.SerializedName

data class LeaveRequest(
    @SerializedName("user_id")
    var user_id: String = "",
    @SerializedName("date")
    var date: String = "",
    @SerializedName("reason")
    var reason: String = "",
    @SerializedName("remarks")
    var remarks: String = "",
)
