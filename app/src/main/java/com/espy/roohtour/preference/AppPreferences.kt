package com.espy.roohtour.preference

object AppPreferences {

    private const val USER_ID = "user_id"
    private const val SHOP_IN_ID = "shop_in_id"
    private const val ORDER_DISCOUNT = "order_disc"
    private const val SERVER_IMAGES_LIST_SIZE = "server_img_list_size"


    var userId: String by PreferenceProvider(USER_ID, "")

    var shopInId: String by PreferenceProvider(SHOP_IN_ID, "")

    var orderDiscount: Double by PreferenceProvider(ORDER_DISCOUNT, 0.0)

    var serverImagesSize: Int by PreferenceProvider(SERVER_IMAGES_LIST_SIZE, 0)
}