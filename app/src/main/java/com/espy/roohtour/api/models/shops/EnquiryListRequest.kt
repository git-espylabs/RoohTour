package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class EnquiryListRequest(

    @SerializedName("exe_id")
    var exe_id: String = "",
    @SerializedName("agency_id")
    var agency_id: String = ""
)