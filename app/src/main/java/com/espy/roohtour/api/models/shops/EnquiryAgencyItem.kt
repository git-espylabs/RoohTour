package com.espy.roohtour.api.models.shops

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class EnquiryAgencyItem(
    @SerialName("shop_name")
    var shop_name: String? = "",
    @SerialName("lattitude")
    var lattitude: String? = "",
    @SerialName("longitude")
    var longitude: String? = "",
    @SerialName("shop_primary_number")
    var shop_primary_number: String? = "",
    @SerialName("shop_secondary_number")
    var shop_secondary_number: String? = "",
    @SerialName("route_name")
    var route_name: String? = "",
    @SerialName("shop_address")
    var shop_address:String?="",
    @SerialName("shop_image")
    var shop_image:String?="",
    @SerialName("added_by")
    var added_by:String?="",
    @SerialName("agency_id")
    var agency_id:String?="",
    @SerialName("rt_short_name")
    var rt_short_name:String?="",
    @SerialName("rt_email")
    var rt_email:String?=""
): Parcelable
