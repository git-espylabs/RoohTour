package com.espy.roohtour.ui.products.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class TodayMyOrder(
    @SerialName("id")
    var id: String? = "",
    var product_name: String? = "",
    @SerialName("totalqty")
    var totalqty: String? = ""
): Parcelable {
    override fun toString(): String {
        return product_name?: ""
    }
}
