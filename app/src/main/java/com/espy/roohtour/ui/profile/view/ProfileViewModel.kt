package com.espy.roohtour.ui.profile.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.espy.roohtour.api.MultiPartRequestHelper
import com.espy.roohtour.api.models.common.ExpenseType
import com.espy.roohtour.api.models.login.LeaveRequest
import com.espy.roohtour.repository.ProfileRepository
import com.espy.roohtour.api.models.login.LoginResponse
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileViewModel : BaseViewModel() {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val  profileRepository = ProfileRepository()

    private val _loginsResponse: MutableLiveData<com.espy.roohtour.api.Result<LoginResponse>> = MutableLiveData()
    val loginsResponse: LiveData<com.espy.roohtour.api.Result<LoginResponse>>
    get() = _loginsResponse

    private val _leaveApplicationResponse: MutableLiveData<Boolean> = MutableLiveData()
    val leaveApplicationResponse: LiveData<Boolean>
        get() = _leaveApplicationResponse

    private val _expenseTypesList: MutableLiveData<com.espy.roohtour.api.Result<List<ExpenseType>>> = MutableLiveData()
    val expenseTypesList: LiveData<com.espy.roohtour.api.Result<List<ExpenseType>>>
        get() = _expenseTypesList

    private val _addExpenseStatus: MutableLiveData<Boolean> = MutableLiveData()
    val addExpenseStatus: LiveData<Boolean>
        get() = _addExpenseStatus


    init {
        if (InstanceManager.capExpenseTypes.isEmpty()){
            getExpenseTypes()
        }
    }

    fun processUserLogin(username: String, password: String){
        viewModelScope.launch {
            profileRepository.processUserLogin(username, password).collect {
                _loginsResponse.value = it
            }
        }
    }

    fun applyLeave(date: String, remarks: String, reason: String){
        val leaveRequest = LeaveRequest(AppPreferences.userId, date, reason, remarks)
        viewModelScope.launch {
            profileRepository.applyLeave(leaveRequest).collect {
                _leaveApplicationResponse.value = it
            }
        }
    }

    private fun getExpenseTypes(){
        viewModelScope.launch {
            profileRepository.getExpenseTypes().collect {
                _expenseTypesList.value = it
            }
        }
    }

    fun submitExpense(type_id: String, amount:String, note: String, imagepath: String, context: Context){
        val partTypeId = MultiPartRequestHelper.createRequestBody("type_id", type_id)
        val partAmount = MultiPartRequestHelper.createRequestBody("amount", amount)
        val partNote = MultiPartRequestHelper.createRequestBody("note", note)
        val partExpenseType = MultiPartRequestHelper.createRequestBody("expense_type", "1")
        val partAddedBy = MultiPartRequestHelper.createRequestBody("added_by", AppPreferences.userId)
        val partFile = MultiPartRequestHelper.createFileRequestBody(imagepath, "image", context)
        viewModelScope.launch {
            profileRepository.submitExpense(partTypeId, partAmount, partNote,  partExpenseType, partAddedBy, partFile).collect {
                _addExpenseStatus.value = it
            }
        }
    }

}