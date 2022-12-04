package com.espy.roohtour.ui.shops.models

import okhttp3.MultipartBody

data class NewShopMultiPartData(
    var shopName: MultipartBody.Part,
    var shopRegNo: MultipartBody.Part,
    var shopPrimaryNo: MultipartBody.Part,
    var shopSecondaryNo: MultipartBody.Part,
    var shopAddress: MultipartBody.Part,
    var shopLat: MultipartBody.Part,
    var shopLon: MultipartBody.Part,
    var shopEmail: MultipartBody.Part,
    var shopRoute: MultipartBody.Part,
    var shopImgPath: MultipartBody.Part,
    var shopOutstanding: MultipartBody.Part,
    var loginId: MultipartBody.Part,
    var added_by: MultipartBody.Part,

    ///////////////////////////////////////

    var shortName: MultipartBody.Part,
    var referenceNumber: MultipartBody.Part,
    var email: MultipartBody.Part,
    var contactPerson: MultipartBody.Part,
    var categoryId: MultipartBody.Part,



)
