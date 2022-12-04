package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class ShopListRequest(
    @SerializedName("exe_id")
    var exe_id: String
)
