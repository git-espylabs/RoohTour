package com.espy.roohtour.api

internal class HttpEndPoints {

    companion object{
        const val WS_URL_PRODUCTION = "https://rooh.espylabs.com/public/"

        //Common Endpoints
        const val CLENSA_LOGIN = "api/login"
        const val CLENSA_PAY_TYPES = "api/paymemnt_type"
        const val IMAGE_ASSETS = WS_URL_PRODUCTION + "assets/images/"
        const val CLENSA_PROFILE = "api/peofileexe"
        const val CLENSA_APPLY_LEAVE = "api/leaveinsert"
        const val CLENSA_EXPENSE_TYPES = "api/expense_types"
        const val CLENSA_ADD_EXPENSE = "api/expenseinsert"

        //Product Endpoints
        const val CLENSA_PRODUCTS_LIST = "api/productall"
        const val CLENSA_CATEGORIES_LIST = "api/category"
        const val CLENSA_PRODUCTS_BY_CATEGORY = "api/productlist"
        const val CLENSA_PRODUCT_IMAGES = "api/product_images"
        const val CLENSA_PRODUCT_LIVE_STOCK = "api/livestock"
        const val CLENSA_PRODUCT_IMAGE_SYNC = "api/all_product_images"

        //Order Endpoints
        const val CLENSA_ORDER_POST = "api/placeorder"

        //Attendance Endpoints
        const val CLENSA_ATTENDANCE_IN = "api/punch_in"
        const val CLENSA_ATTENDANCE_OUT = "api/punchout"

        //Shop Endpoints
        const val CLENSA_SHOPS_LIST = "api/shoplist"
        const val CLENSA_SHOP_FEEDBACK = "api/shopfeedbk"
        const val CLENSA_SHOP_OUTSTANDING = "api/oustanding"
        const val CLENSA_SHOP_PAY_COLLECTION = "api/paymentcollection"
        const val CLENSA_SHOP_IN = "api/shopin"
        const val CLENSA_SHOP_OUT = "api/shopout"
        const val CLENSA_SHOP_ADD_SHOP = "api/add_shop"
        const val CLENSA_ROUTE_LIST = "api/route_list"
        const val CLENSA_DELIVERY_SHOPS_LIST = "api/deliveryshop_orders"
        const val CLENSA_PENDING_ORDERS = "api/salesexe_orders"
        const val CLENSA_PENDING_ORDER_ITEMS = "api/order_items"
        const val CLENSA_DELIVERY_SHOPS_LIST2 = "api/salesexes_shop_orders"
        const val CLENSA_TODAY_MY_ORDERS_LIST = "api/todaymyorders"
        const val CLENSA_SHOP_COLLECTION_HISTORY_LIST = "api/shopcollectionhistory"

        const val ROOH_ADD_ENQUIRY = "api/create_enquiry"
        const val ROOH_ENQUIRY_AGENCY_LIST = "api/enquiry_in_agencies"
        const val ROOH_ENQUIRY_LIST_OF_AGENCY = "api/agency_enquiries_list"
        const val ROOH_ENQUIRY_FOLLOW_UP = "api/create_enquiry_followup"
        const val ROOH_ENQUIRY_FOLLOW_UP_HISTORY = "api/enquiry_followups"
    }

}