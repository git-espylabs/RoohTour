package com.espy.roohtour.api.models.products

import com.google.gson.annotations.SerializedName

data class TodayMyOrderRequest(
    @SerializedName("staffid")
    var staffid: String = ""
)
