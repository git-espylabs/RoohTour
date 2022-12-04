package com.espy.roohtour.ui.order.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class OrderProduct(
    @SerialName("id")
    var id: String? = "",
    @SerialName("category_id")
    var category_id: String? = "",
    @SerialName("product_name")
    var product_name: String? = "",
    @SerialName("description")
    var description: String? = "",
    @SerialName("product_code")
    var product_code: String? = "",
    @SerialName("brand_id")
    var brand_id: String? = "",
    @SerialName("unit_id")
    var unit_id: String? = "",
    @SerialName("qty")
    var qty: String = "0",
    @SerialName("product_price")
    var product_price: String = "0.00",
    @SerialName("batchcode")
    var batchcode: String? = "",
    @SerialName("thumbnail")
    var thumbnail: String? = "",
    @SerialName("stock")
    var stock: String? = "",
    @SerialName("batchid")
    var batchid: String? = ""
): Parcelable {
    override fun toString(): String {
        return product_name?: "-$batchcode"
    }
}
