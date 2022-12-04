package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class ShopInRequest(
    @SerializedName("login_id")
    var login_id: String = "",
    @SerializedName("shop_login_id")
    var shop_login_id: String = "",
    @SerializedName("shopin")
    var shopin: String = "",
    @SerializedName("shopin_lat")
    var shopin_lat: String = "0.0",
    @SerializedName("shop_inlong")
    var shop_inlong: String = "0.0",
)
