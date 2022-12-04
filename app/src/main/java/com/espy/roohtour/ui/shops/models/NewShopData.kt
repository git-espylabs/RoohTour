package com.espy.roohtour.ui.shops.models

data class NewShopData(
    var shopName: String = "0",
    var shopRegNo: String = "0",
    var shopPrimaryNo: String = "0",
    var shopSecondaryNo: String = "0",
    var shopAddress: String = "0",
    var shopLat: String = "0.0",
    var shopLon: String = "0.0",
    var shopEmail: String = "0",
    var shopRoute: String = "0",
    var shopImgPath: String? = null,

    ////new fields


    var shortName: String = "0",
    var referenceNumber: String = "0",
    var email: String = "0",
    var contactPerson: String = "0",
    var categoryId: String = "0",

)
