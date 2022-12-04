package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class DeliveryShopRequest(
    @SerializedName("order_status")
    var order_status: String = "",
    @SerializedName("exe_id")
    var exe_id: String = ""
)
