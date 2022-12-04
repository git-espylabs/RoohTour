package com.espy.roohtour.api.models.shops

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class Shop(
    @SerialName("id")
    var id: String? = "",
    @SerialName("shop_name")
    var shop_name: String? = "",
    @SerialName("lattitude")
    var lattitude: String? = "",
    @SerialName("longitude")
    var longitude: String? = "",
    @SerialName("shop_address")
    var shop_address: String? = "",
    @SerialName("shop_primary_number")
    var shop_primary_number: String? = "",
    @SerialName("shop_secondary_number")
    var shop_secondary_number: String? = "",
    @SerialName("shop_image")
    var shop_image: String? = "",
    @SerialName("shop_oustanding_amount")
    var shop_oustanding_amount: String? = "",
    @SerialName("created_at")
    var created_at: String? = "",
    @SerialName("updated_at")
    var updated_at: String? = "",
    @SerialName("route_id")
    var route_id: String? = "",
    @SerialName("added_by")
    var added_by: String? = "",
    @SerialName("login_id")
    var login_id: String? = "",
    @SerialName("route_name")
    var route_name: String? = ""
): Parcelable{
    override fun toString(): String {
        return shop_name?:"Cap Shop"
    }
}