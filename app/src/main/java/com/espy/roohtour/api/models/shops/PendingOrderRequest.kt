package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class PendingOrderRequest(
    @SerializedName("exe_id")
    var delivery_boy_id: String = "",
    @SerializedName("order_status")
    var order_status: String = "",
    @SerializedName("shop_id")
    var shop_id: String = ""
)

