package com.espy.roohtour.api.models.shops

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class EnquiryItem(
    @SerialName("destination")
    var destination: String? = "",
    @SerialName("date_of_travel")
    var date_of_travel: String? = "",
    @SerialName("comment")
    var comment: String? = "",
    @SerialName("adult")
    var adult: String? = "",
    @SerialName("child")
    var child: String? = "",
    @SerialName("duration")
    var duration: String? = "",
    @SerialName("quotation_amount")
    var quotation_amount: String? = "",
    @SerialName("reminder_date")
    var reminder_date: String? = "",
    @SerialName("notes")
    var notes: String? = "",
    @SerialName("recieve_date")
    var recieve_date: String? = "",
    @SerialName("agency_name")
    var agency_name: String? = "",
    @SerialName("enquiry_id")
    var enquiry_id: String? = "",
): Parcelable
