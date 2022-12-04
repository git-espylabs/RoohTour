package com.espy.roohtour.repository

import com.espy.roohtour.api.RestServiceProvider
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.common.ExpenseType
import com.espy.roohtour.api.models.login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody

class ProfileRepository: BaseRepository() {

    override fun onCleared() {
    }

    suspend fun processUserLogin(username: String, password: String): Flow<Result<LoginResponse>>{
        return flow {
            try {
                val loginRequest = LoginRequest(username, password)
                val response = RestServiceProvider
                    .getCapService()
                    .loginUserAsync(loginRequest)
                    .await()

                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProfile(profileRequest: ProfileRequest): Flow<Result<ProfileResponse>>{
        return flow {
            try {
                val response = RestServiceProvider
                    .getCapService()
                    .getProfileAsync(profileRequest)
                    .await()

                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun applyLeave(leaveRequest: LeaveRequest): Flow<Boolean>{
        return flow {
            try {
                val response = RestServiceProvider
                    .getCapService()
                    .applyLeaveAsync(leaveRequest)
                    .await()

                if (response.isError.not() && response.getMessage().equals("Success", true)) {
                    emit(true)
                } else {
                    emit(false)
                }
            } catch (e: Exception) {
                emit(false)
            }
        }.flowOn(Dispatchers.IO)
    }


    fun getExpenseTypes(): Flow<Result<List<ExpenseType>>> {
        return flow {
            try {
                val response = RestServiceProvider
                    .getCapService()
                    .getExpenseTypesAsync()
                    .await()

                emit(Result.Success(response.data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun submitExpense(type_id: MultipartBody.Part, amount: MultipartBody.Part, note: MultipartBody.Part, expense_type: MultipartBody.Part, added_by: MultipartBody.Part, image: MultipartBody.Part): Flow<Boolean>{
        return flow {
            try {
                val response =
                    RestServiceProvider
                        .getCapService()
                        .submitExpenseAsync(type_id, amount, note, expense_type, added_by, image)
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

}