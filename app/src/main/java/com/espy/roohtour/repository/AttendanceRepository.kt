package com.espy.roohtour.repository

import com.espy.roohtour.api.RestServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody

class AttendanceRepository: BaseRepository() {
    override fun onCleared() {

    }

    suspend fun processAttendance(loginId: MultipartBody.Part, status: MultipartBody.Part, image: MultipartBody.Part, punchStatus:String): Flow<String>{
        return flow {
            try {
                val response = if (punchStatus == "0") {
                    RestServiceProvider
                        .getAttendanceService()
                        .markAttendanceInAsync(loginId, status, image)
                        .await()
                } else {
                    RestServiceProvider
                        .getAttendanceService()
                        .markAttendanceOutAsync(loginId, status, image)
                        .await()
                }

                if (response.isError.not()){
                    emit(response.getMessage())
                }else{
                    emit(response.getMessage())
                }
            } catch (e: Exception) {
                emit("Request Failed! Try again later")
            }
        }.flowOn(Dispatchers.IO)
    }
}