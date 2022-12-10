package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

data class AddEnquiryDataRequest(
    @SerializedName("agency_id")
    var agency_id: String = "",

    @SerializedName("staff_login_id")
    var staff_login_id: String = "",

    @SerializedName("destination")
    var destination: String = "",

    @SerializedName("date_of_travel")
    var date_of_travel: String = "",

    @SerializedName("comment")
    var comment: String = "",

    @SerializedName("adult")
    var adult: String = "",


    @SerializedName("child")
    var child: String = "",

    @SerializedName("duration")
    var duration: String = "",

    @SerializedName("quotation_amount")
    var quotation_amount: String = "",

    @SerializedName("reminder_date")
    var reminder_date: String = "",

    @SerializedName("notes")
    var notes: String = ""
)
