package com.espy.roohtour.ui.home

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.espy.roohtour.api.MultiPartRequestHelper
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.login.Profile
import com.espy.roohtour.api.models.login.ProfileRequest
import com.espy.roohtour.api.models.products.Category
import com.espy.roohtour.repository.ProductRepository
import com.espy.roohtour.repository.ShopRepository
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.api.models.products.ProductImageRequest
import com.espy.roohtour.api.models.products.ProductsByCategoryRequest
import com.espy.roohtour.api.models.shops.*
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.domain.toImageSlide
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.repository.OrderRepository
import com.espy.roohtour.repository.ProfileRepository
import com.espy.roohtour.ui.base.BaseViewModel
import com.espy.roohtour.ui.products.models.ImageSlide
import com.espy.roohtour.ui.products.models.TodayMyOrder
import com.espy.roohtour.ui.shops.models.ShopPayHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeViewModel : BaseViewModel() {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val shopRepository = ShopRepository()
    private val productRepository = ProductRepository()
    private val profileRepository = ProfileRepository()
    val orderRepository = OrderRepository()

    private val _categoriesList: MutableLiveData<Result<List<Category>>> = MutableLiveData()
    val categoriesList: LiveData<Result<List<Category>>>
        get() = _categoriesList

    private val _productsByCategory: MutableLiveData<Result<List<Product>>> = MutableLiveData()
    val productsByCategory: LiveData<Result<List<Product>>>
        get() = _productsByCategory

    var _isShoppedIn: MutableLiveData<Shop> = MutableLiveData()
    val isShoppedIn: LiveData<Shop>
        get() = _isShoppedIn

    var _isShoppedOut: MutableLiveData<Boolean> = MutableLiveData()
    val isShoppedOut: LiveData<Boolean>
        get() = _isShoppedOut

    private val _myProfile: MutableLiveData<Profile> = MutableLiveData()
    val myProfile: LiveData<Profile>
        get() = _myProfile

    private val _slideImages: MutableLiveData<List<ImageSlide>> = MutableLiveData()
    val slideImages: LiveData<List<ImageSlide>>
        get() = _slideImages

    private val _expenseSubmitStatus: MutableLiveData<Boolean> = MutableLiveData()
    val expenseSubmitStatus: LiveData<Boolean>
        get() = _expenseSubmitStatus

    private val _allShops: MutableLiveData<List<Shop>> = MutableLiveData()
    val allShops: LiveData<List<Shop>>
        get() = _allShops

    var _deliveryShopsList: MutableLiveData<com.espy.roohtour.api.Result<List<DeliveryShop>>> = MutableLiveData()
    val deliveryShopsList: LiveData<com.espy.roohtour.api.Result<List<DeliveryShop>>>
        get() = _deliveryShopsList

    var _addNewShop: MutableLiveData<Boolean> = MutableLiveData()
    val addNewShop: LiveData<Boolean>
        get() = _addNewShop

    var _shopRoutes: MutableLiveData<List<Route>> = MutableLiveData()
    val shopRoutes: LiveData<List<Route>>
        get() = _shopRoutes

    var myShops = listOf<Shop>()
    var myCategories = listOf<Category>()


    private val _todayMyOrders: MutableLiveData<Result<List<TodayMyOrder>>> = MutableLiveData()
    val todayMyOrders: LiveData<Result<List<TodayMyOrder>>>
        get() = _todayMyOrders

    private val _shopCollectionHistoryList: MutableLiveData<Result<List<ShopPayHistory>>> = MutableLiveData()
    val shopCollectionHistoryList: LiveData<Result<List<ShopPayHistory>>>
        get() = _shopCollectionHistoryList

    init {
        if (null == InstanceManager.profile){
            getProfile()
        }
    }

    fun tagShopIn(shop: Shop, location: Location?){
        val shopInRequest = ShopInRequest(AppPreferences.userId, shop.id?:"0", "1", location?.latitude?.toString()?:"0.0", location?.longitude?.toString()?:"0.0")
        viewModelScope.launch {
            shopRepository.tagShopIn(shopInRequest).collect {
                if (it) {
                    _isShoppedIn.value = shop
                } else {
                    _isShoppedIn.value = null
                }
            }
        }
    }

    fun tagShopOut(shopid: String, location: Location?){
        val shopOutRequest = ShopOutRequest(AppPreferences.userId, shopid, "1", location?.latitude?.toString()?:"0.0", location?.longitude?.toString()?:"0.0")
        viewModelScope.launch {
            shopRepository.tagShopOut(shopOutRequest).collect {
                _isShoppedOut.value = it
            }
        }
    }

    fun getCategoriesList(){
        viewModelScope.launch {
            productRepository.getCategoriesList().collect {
                _categoriesList.value = it
            }
        }
    }

    fun getProductsByCategory(categoryId: String){
        val productsByCategory = ProductsByCategoryRequest(categoryId)
        viewModelScope.launch {
            productRepository.getProductByCategory(productsByCategory).collect {
                _productsByCategory.value = it
            }
        }
    }

    fun getProductsFromDbByCategory(categoryId: String){
        viewModelScope.launch {
            productRepository.getProductFromDbByCategory(categoryId).collect {
                _productsByCategory.value = it
            }
        }
    }
    private fun getProfile(){
        val profileReqst = ProfileRequest(AppPreferences.userId)
        viewModelScope.launch {
            profileRepository.getProfile(profileReqst).collect {
                if (it is Result.Success && it.data.data.any()){
                    _myProfile.value = it.data.data[0]
                }
                if (InstanceManager.capExpenseTypes.isEmpty()){
                    getExpenseTypes()
                }
            }
        }
    }

    fun getProductsImages(productId: String){
        val productImageRequest = ProductImageRequest(productId)
        viewModelScope.launch {
            productRepository.getProductImages(productImageRequest).collect {
                if (it is Result.Success && it.data.any()){
                    val imageSlides = it.data.map { pdtImg ->
                        pdtImg.toImageSlide()
                    }
                    _slideImages.value = imageSlides
                }else{
                    _slideImages.value = listOf()
                }
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
                _expenseSubmitStatus.value = it
            }
        }
    }

    private fun getExpenseTypes(){
        viewModelScope.launch {
            profileRepository.getExpenseTypes().collect {
                if (it is Result.Success && it.data.any()){
                    InstanceManager.capExpenseTypes = it.data
                }
            }
        }
    }

    fun getShopsList(){
        viewModelScope.launch {
            shopRepository.getAllShopsFromDb().collect {
                if (it is Result.Success){
                    _allShops.value = it.data
                }
            }
        }
    }

    fun emptyCart(){
        orderRepository.emptyCart()
    }


    fun getDeliveryShopsList(orderStatus: String){
        viewModelScope.launch {
            shopRepository.getDeliveryShopsList(orderStatus).let {
                _deliveryShopsList.value = it
            }
        }
    }


    fun getTodayMyOrdersList(){
        viewModelScope.launch {
            shopRepository.getTodayMyOrdersList().let {
                _todayMyOrders.value = it
            }
        }
    }


    fun getShopCollectionHistory(shopId: String, fromDate: String, toDate: String){
        viewModelScope.launch {
            shopRepository.getShopCollectionHistory(ShopCollectionHistoryRequest(shopId, fromDate, toDate)).let {
                _shopCollectionHistoryList.value = it
            }
        }
    }
}