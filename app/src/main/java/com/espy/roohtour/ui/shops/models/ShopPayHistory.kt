package com.espy.roohtour.ui.shops.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class ShopPayHistory(
    @SerialName("shop_login_id")
    var shop_login_id: String? = "",

    @SerialName("amount")
    var amount: String? = "0",

    @SerialName("pay_remark")
    var pay_remark: String? = "0",

    @SerialName("transaction_id")
    var transaction_id: String? = "0",

    @SerialName("cheque_number")
    var cheque_number: String? = "0",

    @SerialName("date")
    var date: String? = "",

    @SerialName("cheque_clear_date")
    var cheque_clear_date: String? = "0",

    @SerialName("cheque_clear_status")
    var cheque_clear_status: String? = "0",

    @SerialName("view")
    var view: String? = "0",

    @SerialName("pay_type")
    var pay_type: String? = "0",

    @SerialName("staff_login_id")
    var staff_login_id: String? = "0",

    @SerialName("staff_name")
    var staff_name: String? = "0",

): Parcelable
