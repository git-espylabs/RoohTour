package com.espy.roohtour.api.services

import com.espy.roohtour.api.HttpEndPoints
import com.espy.roohtour.api.models.ResponseBase
import com.espy.roohtour.api.models.TempResponse
import com.espy.roohtour.api.models.common.ExpenseTypes
import com.espy.roohtour.api.models.common.PaymentTypes
import com.espy.roohtour.api.models.login.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

interface CommonApiInterface {

    @POST(HttpEndPoints.CLENSA_LOGIN)
    fun loginUserAsync(
        @Body loginRequest: LoginRequest
    ): Deferred<LoginResponse>

    @GET(HttpEndPoints.CLENSA_PAY_TYPES)
    fun getPaymentTypesAsync(
    ): Deferred<PaymentTypes>

    @POST(HttpEndPoints.CLENSA_PROFILE)
    fun getProfileAsync(
        @Body profileRequest: ProfileRequest
    ): Deferred<ProfileResponse>

    @POST(HttpEndPoints.CLENSA_APPLY_LEAVE)
    fun applyLeaveAsync(
        @Body leaveRequest: LeaveRequest
    ): Deferred<ResponseBase>

    @POST(HttpEndPoints.CLENSA_EXPENSE_TYPES)
    fun getExpenseTypesAsync(
    ): Deferred<ExpenseTypes>

    @Multipart
    @POST(HttpEndPoints.CLENSA_ADD_EXPENSE)
    fun submitExpenseAsync(
        @Part type_id: MultipartBody.Part,
        @Part amount: MultipartBody.Part,
        @Part note: MultipartBody.Part,
        @Part expense_type: MultipartBody.Part,
        @Part added_by: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Deferred<TempResponse>
}