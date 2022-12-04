package com.espy.roohtour.repository

import com.espy.roohtour.api.RestServiceProvider
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.common.PaymentType
import com.espy.roohtour.api.models.products.TodayMyOrderRequest
import com.espy.roohtour.api.models.shops.*
import com.espy.roohtour.db.DatabaseProvider
import com.espy.roohtour.db.entities.ShopsEntity
import com.espy.roohtour.domain.toDomain
import com.espy.roohtour.domain.toEntity
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.ui.products.models.TodayMyOrder
import com.espy.roohtour.ui.shops.models.NewShopMultiPartData
import com.espy.roohtour.ui.shops.models.ShopPayHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody

class ShopRepository: BaseRepository() {

    override fun onCleared() {
    }

    private val shopsDao = DatabaseProvider.getShopsDao()

    fun getShopsListFromServer(): Flow<Result<List<Shop>>> {
        return flow {
            try {
                val response = RestServiceProvider
                    .getShopService()
                    .getAllShopSlistAsync(ShopListRequest(AppPreferences.userId))
                    .await()

                if (response.data.any()){
                    val shps = response.data.map {s->
                        s.toEntity()
                    }
                    insertShopsToDb(shps)

                    shopsDao.getShops().collect {
                        val dbList = it.map { shEntity ->
                            shEntity.toDomain()
                        }
                        emit(Result.Success(dbList))
                    }
                }else{
                    emit(Result.Error(Exception("error")))
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun postFeedBack(feedbackRequest: FeedbackRequest): Flow<Boolean>{
        return flow {
            try {
                val response = RestServiceProvider
                    .getShopService()
                    .sendFeedbackAsync(feedbackRequest)
                    .await()

                if (response.isError.not()) {
                    emit(true)
                } else {
                    emit(false)
                }
            } catch (e: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getShopOutstanding(outstandingRequest: OutstandingRequest): Flow<Result<List<ShopOutstanding>>> {
        return flow {
            try {
                val response = RestServiceProvider
                    .getShopService()
                    .getShopOutstandingAsync(outstandingRequest)
                    .await()

                emit(Result.Success(response.data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getPaymentTypes(): Flow<Result<List<PaymentType>>> {
        return flow {
            try {
                val response = RestServiceProvider
                    .getCapService()
                    .getPaymentTypesAsync()
                    .await()

                emit(Result.Success(response.data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun submitPaymentCollection(partShopId: MultipartBody.Part, amount: MultipartBody.Part, loginId: MultipartBody.Part, pay_type: MultipartBody.Part, image: MultipartBody.Part, chequenumber: MultipartBody.Part, chequedate: MultipartBody.Part): Flow<Boolean>{
        return flow {
            try {
                val response =
                    RestServiceProvider
                        .getShopService()
                        .submitPaymentCollectionAsync(partShopId, amount, loginId, pay_type, image,chequenumber, chequedate)
                        .await()

                if (response.isError.not()){
                    emit(true)
                }else{
                    emit(false)
                }
            } catch (e: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun tagShopIn(shopInRequest: ShopInRequest): Flow<Boolean>{
        return flow {
            try {
                val response =
                    RestServiceProvider
                        .getShopService()
                        .shopInAsync(shopInRequest)
                        .await()

                if (response.isError.not()){
                    emit(true)
                }else{
                    emit(false)
                }
            } catch (e: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun tagShopOut(shopOutRequest: ShopOutRequest): Flow<Boolean>{
        return flow {
            try {
                val response =
                    RestServiceProvider
                        .getShopService()
                        .shopOutAsync(shopOutRequest)
                        .await()

                if ((response.isError.not() && response.getMessage().equals("Success", true)) || response.getMessage().equals("Already shopout", true)){
                    emit(true)
                }
                else{
                    emit(false)
                }
            } catch (e: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun insertShopsToDb(list: List<ShopsEntity>){
        shopsDao.saveShops(list)
    }

    fun getAllShopsFromDb(): Flow<Result<List<Shop>>>{
        return flow{
            try {
                shopsDao.getShops().collect {
                    val dbList = it.map { shopsEntity ->
                        shopsEntity.toDomain()
                    }
                    emit(Result.Success(dbList))
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadNewShop(newShopMultiPartData: NewShopMultiPartData): Boolean{

        return try {
            val response =
                RestServiceProvider
                    .getShopService()
                    .uploadNewShopAsync(
                        newShopMultiPartData.loginId,
                        newShopMultiPartData.shopName,
                        newShopMultiPartData.shopRegNo,
                        newShopMultiPartData.shopPrimaryNo,
                        newShopMultiPartData.shopSecondaryNo,
                        newShopMultiPartData.shopAddress,
                        newShopMultiPartData.shopLat,
                        newShopMultiPartData.shopLon,
                        /*newShopMultiPartData.shopEmail,*/
                        newShopMultiPartData.shopRoute,
                        newShopMultiPartData.added_by,
                        newShopMultiPartData.shopOutstanding,
                        newShopMultiPartData.shopImgPath,

                        ///
                    newShopMultiPartData.shortName,
                        newShopMultiPartData.referenceNumber,
                        newShopMultiPartData.email,
                        newShopMultiPartData.contactPerson,
                        newShopMultiPartData.categoryId


                    )
                    .await()

            response.isError.not()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getRoutesList(): Result<List<Route>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getRoutesListAsync()
                .await()

            Result.Success(response.data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getDeliveryShopsList(orderStatus: String): Result<List<DeliveryShop>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getDeliveryShopsListAsync(DeliveryShopRequest(orderStatus, AppPreferences.userId))
                .await()

            if (response.data.any()){
                Result.Success(response.data)
            }else{
                Result.Error(Exception("error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getDeliveryShopsList(fromDate: String, toDate: String): Result<List<DeliveryShop>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getDeliveryShopsListAsync(DeliveryShopsRequest("3", AppPreferences.userId, fromDate, toDate))
                .await()

            if (response.data.any()){
                Result.Success(response.data)
            }else{
                Result.Error(Exception("error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    suspend fun getPendingOrderList(pendingOrderRequest: PendingOrderRequest): Result<List<PendingOrder>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getPendingOrdersAsync(pendingOrderRequest)
                .await()
            response.data?.let {
                Result.Success(it)
            }?: kotlin.run {
                Result.Success(listOf<PendingOrder>())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    suspend fun getPendingOrderItemsList(orderItemRequest: OrderItemRequest): com.espy.roohtour.api.Result<List<OrderItem>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getPendingOrderItemsAsync(orderItemRequest)
                .await()
            response.data?.let {
                Result.Success(it)
            }?: kotlin.run {
                Result.Success(listOf<OrderItem>())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getTodayMyOrdersList(): Result<List<TodayMyOrder>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getTodayMyOrdersAsync(TodayMyOrderRequest( AppPreferences.userId))
                .await()

            if (response.data.any()){
                Result.Success(response.data)
            }else{
                Result.Error(Exception("error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getShopCollectionHistory(request: ShopCollectionHistoryRequest): Result<List<ShopPayHistory>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getShopCollectionHistoryAsync(request)
                .await()

            if (response.data.any()){
                Result.Success(response.data)
            }else{
                Result.Error(Exception("error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }





    //
    fun addEnquirys(request: AddEnquiryDataRequest): Flow<Boolean> {
        //val jsonString = Gson().toJson(request)
        return flow {
            try {
                val response = RestServiceProvider
                    .getShopService()
                    .addEnquiryAsync(request)
                    .await()

                if (response.isError.not()) {
                    emit(true)
                } else {
                    emit(false)
                }
            } catch (e: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEnquiryAgencyList(fromDate: String, toDate: String): Result<List<EnquiryAgencyItem>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getEnquiryAgencyListAsync(EnquiryAgencyListRequest(AppPreferences.userId, fromDate, toDate))
                .await()

            if (response.data.any()){
                Result.Success(response.data)
            }else{
                Result.Error(Exception("error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getEnquiryOfAgencyListAsync(agencyId: String): Result<List<EnquiryItem>> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .getEnquiryOfAgencyListAsync(EnquiryListRequest(AppPreferences.userId, agencyId))
                .await()

            if (response.data.any()){
                Result.Success(response.data)
            }else{
                Result.Error(Exception("error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun followUpEnquiry(
        agency_id: String,
        enquiry_id: String,
        followup_date: String,
        confirm_chance: String,
        amendment_replied_date: String,
        notes: String
    ): Result<Int> {
        return try {
            val response = RestServiceProvider
                .getShopService()
                .followupEnquiryAsync(FollowupEnquiryRequest(agency_id, enquiry_id, followup_date, confirm_chance, amendment_replied_date, notes))
                .await()

            if (response.data != 0){
                Result.Success(response.data as Int)
            }else{
                Result.Error(Exception("error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}