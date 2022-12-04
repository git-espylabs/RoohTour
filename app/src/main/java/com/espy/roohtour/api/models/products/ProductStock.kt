package com.espy.roohtour.api.models.products

import kotlinx.serialization.SerialName

data class ProductStock(
    @SerialName("id")
    var id: String? = "",
    @SerialName("batch_id")
    var batch_id: String? = "",
    @SerialName("stock")
    var stock: String? = ""
)
