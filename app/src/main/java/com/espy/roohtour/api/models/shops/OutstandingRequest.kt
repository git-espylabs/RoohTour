package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class OutstandingRequest(
    @SerializedName("shop_id")
    var shop_id: String = ""
)
