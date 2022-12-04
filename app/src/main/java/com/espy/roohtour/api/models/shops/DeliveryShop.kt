package com.espy.roohtour.api.models.shops

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class DeliveryShop(
    @SerialName("orderid")
    var orderid: String? = "",
    @SerialName("order_status")
    var order_status: String? = "",
    @SerialName("invoice_id")
    var invoice_id: String? = "",
    @SerialName("shopid")
    var shopid: String? = "",
    @SerialName("shop_name")
    var shop_name: String? = "",
    @SerialName("shop_address")
    var shop_address: String? = "",
    @SerialName("lattitude")
    var lattitude: String? = "",
    @SerialName("longitude")
    var longitude: String? = "",
    @SerialName("shop_primary_number")
    var shop_primary_number: String? = "",
    @SerialName("route_name")
    var route_name: String? = "",
    @SerialName("shop_regi_no")
    var shop_regi_no: String? = ""
): Parcelable {
    override fun toString(): String {
        return shop_name?:"Cap Shop"
    }
}
