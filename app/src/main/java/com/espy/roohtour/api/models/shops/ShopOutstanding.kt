package com.espy.roohtour.api.models.shops

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class ShopOutstanding(
    @SerialName("id")
    var id: String? = "",
    @SerialName("shop_oustanding_amount")
    var shop_oustanding_amount: String? = ""
): Parcelable
