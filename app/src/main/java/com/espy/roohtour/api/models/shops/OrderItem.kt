package com.espy.roohtour.api.models.shops

import com.google.gson.annotations.SerializedName

class OrderItem(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("order_id")
    var order_id: String = "",
    @SerializedName("batch_id")
    var batch_id: String = "",
    @SerializedName("qty")
    var qty: String = "",
    @SerializedName("total_amount")
    var total_amount: String = "",
    @SerializedName("product_name")
    var product_name: String = "",
    @SerializedName("price")
    var price: String = "",
    @SerializedName("mrp")
    var mrp: String? = ""
){
    override fun toString(): String {
        return product_name
    }
}