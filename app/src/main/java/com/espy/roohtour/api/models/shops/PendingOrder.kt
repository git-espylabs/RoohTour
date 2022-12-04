package com.espy.roohtour.api.models.shops

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PendingOrder(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("invoice_id")
    var invoice_id: String = "",
    @SerializedName("shop_id")
    var shop_id: String = "",
    @SerializedName("date")
    var date: String = "",
    @SerializedName("total_amount")
    var total_amount: String = "",
    @SerializedName("total_qty")
    var total_qty: String = "",
    @SerializedName("added_id")
    var added_id: String = "",
    @SerializedName("order_status")
    var order_status: String = "",
    @SerializedName("order_status_updated_by")
    var order_status_updated_by: String = "",
    @SerializedName("assign_deliveryboy_id")
    var assign_deliveryboy_id: String = ""
): Parcelable {
    override fun toString(): String {
        return "Order No: $id"
    }
}
