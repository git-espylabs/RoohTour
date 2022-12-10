package com.espy.roohtour.api.models.shops

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName


@Parcelize
data class FollowupHistoryItem(
    @SerialName("id")
    var id: String? = "",
    @SerialName("agency_id")
    var agency_id: String? = "",
    @SerialName("enquiry_id")
    var enquiry_id: String? = "",
    @SerialName("followup_date")
    var followup_date: String? = "",
    @SerialName("confirm_chance")
    var confirm_chance: String? = "",
    @SerialName("amendment_replied_date")
    var amendment_replied_date: String? = "",
    @SerialName("notes")
    var notes: String? = "",
    @SerialName("added_date")
    var added_date: String? = ""
): Parcelable