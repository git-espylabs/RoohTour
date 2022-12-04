package com.espy.roohtour.api.models.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class PaymentType(
    @SerialName("id")
    var id: String? = "",
    @SerialName("payment_type")
    var payment_type: String? = ""
): Parcelable{
    override fun toString(): String {
        return payment_type.toString()
    }
}


