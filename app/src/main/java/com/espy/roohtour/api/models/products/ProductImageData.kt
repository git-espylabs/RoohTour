package com.espy.roohtour.api.models.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class ProductImageData(
    @SerialName("id")
    var id: String? = "",
    @SerialName("product_id")
    var product_id: String? = "",
    @SerialName("image")
    var image: String? = ""
): Parcelable
