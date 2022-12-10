package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class FollowupHistoryRequest(

    @SerializedName("enquiry_id")
    var enquiry_id: String = "",
    @SerializedName("agency_id")
    var agency_id: String = ""
)
