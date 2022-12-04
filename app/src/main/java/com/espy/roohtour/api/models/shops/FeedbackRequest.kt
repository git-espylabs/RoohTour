package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class FeedbackRequest(
@SerializedName("feedback")
var feedback: String = "",
@SerializedName("description")
var description: String = "",
@SerializedName("staff_login_id")
var staff_login_id: String = "",
@SerializedName("shop_login_id")
var shop_login_id: String = "",
@SerializedName("latitude")
var latitude: String = "",
@SerializedName("longitude")
var longitude: String = ""
)