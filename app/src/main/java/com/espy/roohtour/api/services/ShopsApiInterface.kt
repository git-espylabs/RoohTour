package com.espy.roohtour.api.services

import com.espy.roohtour.api.HttpEndPoints
import com.espy.roohtour.api.models.ResponseBase
import com.espy.roohtour.api.models.TempResponse
import com.espy.roohtour.api.models.products.TodayMyOrderRequest
import com.espy.roohtour.api.models.products.TodayMyOrdersResp
import com.espy.roohtour.api.models.shops.*
import com.espy.roohtour.api.models.shops.DeliveryShopRequest
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

interface ShopsApiInterface {

    @POST(HttpEndPoints.CLENSA_SHOPS_LIST)
    fun getShopsListAsync(
        @Body shopListRequest: ShopListRequest
    ): Deferred<Shops>

    @POST(HttpEndPoints.CLENSA_SHOP_FEEDBACK)
    fun sendFeedbackAsync(
        @Body feedbackRequest: FeedbackRequest
    ): Deferred<FeedbackResponse>

    @POST(HttpEndPoints.CLENSA_SHOP_OUTSTANDING)
    fun getShopOutstandingAsync(
        @Body outstandingRequest: OutstandingRequest
    ): Deferred<OutstandingResponse>

    @Multipart
    @POST(HttpEndPoints.CLENSA_SHOP_PAY_COLLECTION)
    fun submitPaymentCollectionAsync(
        @Part shop_login_id: MultipartBody.Part,
        @Part amount: MultipartBody.Part,
        @Part staff_login_id: MultipartBody.Part,
        @Part pay_type: MultipartBody.Part,
        @Part image: MultipartBody.Part,
        @Part chequenumber: MultipartBody.Part,
        @Part chequedate: MultipartBody.Part
    ): Deferred<TempResponse>

    @POST(HttpEndPoints.CLENSA_SHOP_IN)
    fun shopInAsync(
        @Body shopInRequest: ShopInRequest
    ): Deferred<ResponseBase>

    @POST(HttpEndPoints.CLENSA_SHOP_OUT)
    fun shopOutAsync(
        @Body shopOutRequest: ShopOutRequest
    ): Deferred<ResponseBase>

    @Multipart
    @POST(HttpEndPoints.CLENSA_SHOP_ADD_SHOP)
    fun uploadNewShopAsync(
        @Part loginId: MultipartBody.Part,
        @Part partShopName: MultipartBody.Part,
        @Part partShopReg: MultipartBody.Part,
        @Part partShopPrimaryNo: MultipartBody.Part,
        @Part partShopSecondaryNo: MultipartBody.Part,
        @Part partShopAddress: MultipartBody.Part,
        @Part partShopLat: MultipartBody.Part,
        @Part partShopLon: MultipartBody.Part,
        /*@Part partShopEmail: MultipartBody.Part,*/
        @Part partShopRoute: MultipartBody.Part,
        @Part addedBy: MultipartBody.Part,
        @Part partShopOutstanding: MultipartBody.Part,
        @Part image: MultipartBody.Part,


        @Part rt_short_name: MultipartBody.Part,
        @Part rt_ref_no: MultipartBody.Part,
        @Part rt_email: MultipartBody.Part,
        @Part rt_contact_person: MultipartBody.Part,
        @Part rt_category: MultipartBody.Part,


    ): Deferred<TempResponse>

    @GET(HttpEndPoints.CLENSA_ROUTE_LIST)
    fun getRoutesListAsync(): Deferred<Routes>

    @POST(HttpEndPoints.CLENSA_DELIVERY_SHOPS_LIST)
    fun getDeliveryShopsListAsync(
        @Body deliveryShopRequest: DeliveryShopRequest
    ): Deferred<DeliveryShops>

    @POST(HttpEndPoints.CLENSA_PENDING_ORDERS)
    fun getPendingOrdersAsync(
        @Body pendingOrders: PendingOrderRequest
    ): Deferred<PendingOrderResponse>

    @POST(HttpEndPoints.CLENSA_PENDING_ORDER_ITEMS)
    fun getPendingOrderItemsAsync(
        @Body orderItems: OrderItemRequest
    ): Deferred<OrderItemResponse>

    @POST(HttpEndPoints.CLENSA_SHOPS_LIST)
    fun getAllShopSlistAsync(
        @Body shopListRequest: ShopListRequest
    ): Deferred<Shops>

    @POST(HttpEndPoints.CLENSA_DELIVERY_SHOPS_LIST2)
    fun getDeliveryShopsListAsync(
        @Body deliveryShopRequest: DeliveryShopsRequest
    ): Deferred<DeliveryShops>

    @POST(HttpEndPoints.CLENSA_TODAY_MY_ORDERS_LIST)
    fun getTodayMyOrdersAsync(
        @Body todayMyOrderRequest: TodayMyOrderRequest
    ): Deferred<TodayMyOrdersResp>

    @POST(HttpEndPoints.CLENSA_SHOP_COLLECTION_HISTORY_LIST)
    fun getShopCollectionHistoryAsync(
        @Body request: ShopCollectionHistoryRequest
    ): Deferred<ShopCollectionHistoryResp>

    //add enquiry

    @POST(HttpEndPoints.ROOH_ADD_ENQUIRY)
    fun addEnquiryAsync(@Body request: AddEnquiryDataRequest): Deferred<AddEnquiryResp>

    //agency list
    @POST(HttpEndPoints.ROOH_ENQUIRY_AGENCY_LIST)
    fun getEnquiryAgencyListAsync(
        @Body enquiryAgencyListRequest: EnquiryAgencyListRequest
    ): Deferred<EnquiryAgencyListResp>

    //enquiry list
    @POST(HttpEndPoints.ROOH_ENQUIRY_LIST_OF_AGENCY)
    fun getEnquiryOfAgencyListAsync(
        @Body enquiryListRequest: EnquiryListRequest
    ): Deferred<EnquiryListResp>

    //enquiry followup
    @POST(HttpEndPoints.ROOH_ENQUIRY_FOLLOW_UP)
    fun followupEnquiryAsync(
        @Body followupEnquiryRequest: FollowupEnquiryRequest
    ): Deferred<FollowupResponse>

    //enquiry followup history
    @POST(HttpEndPoints.ROOH_ENQUIRY_FOLLOW_UP_HISTORY)
    fun followupEnquiryHistoryAsync(
        @Body followupHistoryRequest: FollowupHistoryRequest
    ): Deferred<FollowupHistoryResponse>

}