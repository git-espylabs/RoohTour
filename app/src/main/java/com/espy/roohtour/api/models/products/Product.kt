package com.espy.roohtour.api.models.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class Product(
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
    @SerialName("created_at")
    var created_at: String? = "",
    @SerialName("updated_at")
    var updated_at: String? = "",
    @SerialName("price")
    var price: String? = "",
    @SerialName("batchcode")
    var batchcode: String? = "",
    @SerialName("thumbnail")
    var thumbnail: String? = "",
    @SerialName("stock")
    var stock: String? = "",
    @SerialName("batchid")
    var batchid: String? = "",
    @SerialName("mrp")
    var mrp: String? = ""
): Parcelable {
    override fun toString(): String {
        return product_name?: "-$batchcode"
    }
}
