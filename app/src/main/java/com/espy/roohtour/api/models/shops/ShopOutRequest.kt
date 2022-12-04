package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class ShopOutRequest(
    @SerializedName("login_id")
    var login_id: String = "",
    @SerializedName("shop_login_id")
    var shop_login_id: String = "",
    @SerializedName("shop_out")
    var shop_out: String = "",
    @SerializedName("shopout_lat")
    var shopout_lat: String = "0.0",
    @SerializedName("shopout_long")
    var shopout_long: String = "0.0",
)
