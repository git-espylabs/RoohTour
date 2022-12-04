package com.espy.roohtour.api.services

import com.espy.roohtour.api.HttpEndPoints
import com.espy.roohtour.api.models.ResponseBase
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AttendanceApiInterface {

    @Multipart
    @POST(HttpEndPoints.CLENSA_ATTENDANCE_IN)
    fun markAttendanceInAsync(
        @Part login_id: MultipartBody.Part,
        @Part punch_in: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Deferred<ResponseBase>

    @Multipart
    @POST(HttpEndPoints.CLENSA_ATTENDANCE_OUT)
    fun markAttendanceOutAsync(
        @Part login_id: MultipartBody.Part,
        @Part punch_out: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Deferred<ResponseBase>
}