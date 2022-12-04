package com.espy.roohtour.api.models.shops

import com.espy.roohtour.api.models.ResponseBase
import com.espy.roohtour.ui.shops.models.ShopPayHistory
import kotlinx.serialization.SerialName

data class AddEnquiryResp(
    @SerialName("data")
    var data: Int?=0
) : ResponseBase()
