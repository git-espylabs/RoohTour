package com.espy.roohtour.ui.order.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.espy.roohtour.api.Result
import com.espy.roohtour.domain.toOrderRequestTrans
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.repository.OrderRepository
import com.espy.roohtour.api.models.order.OrderRequestMaster
import com.espy.roohtour.api.models.products.Category
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.api.models.products.ProductImageRequest
import com.espy.roohtour.api.models.products.ProductStockRequest
import com.espy.roohtour.domain.toImageSlide
import com.espy.roohtour.domain.toOrderEntity
import com.espy.roohtour.repository.ProductRepository
import com.espy.roohtour.ui.base.BaseViewModel
import com.espy.roohtour.ui.order.models.OrderProduct
import com.espy.roohtour.ui.products.models.ImageSlide
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class OrderViewModel : BaseViewModel() {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    val orderRepository = OrderRepository()

    val productRepository = ProductRepository()

    var orderShopId = "0"

    var orderProductsList = arrayListOf<OrderProduct?>()

    var myCategories = listOf<Category>()

    private val _orderUpload: MutableLiveData<Boolean> = MutableLiveData()
    val orderUpload: LiveData<Boolean>
        get() = _orderUpload

    private val _allProductsList: MutableLiveData<List<Product>> = MutableLiveData()
    val allProductsList: LiveData<List<Product>>
        get() = _allProductsList

    private val _categoriesList: MutableLiveData<Result<List<Category>>> = MutableLiveData()
    val categoriesList: LiveData<Result<List<Category>>>
        get() = _categoriesList

    private val _productsByCategory: MutableLiveData<Result<List<Product>>> = MutableLiveData()
    val productsByCategory: LiveData<Result<List<Product>>>
        get() = _productsByCategory

   var _slideImages: MutableLiveData<List<ImageSlide>> = MutableLiveData()
    val slideImages: LiveData<List<ImageSlide>>
        get() = _slideImages

    private val _cartCount: MutableLiveData<Int> = MutableLiveData()
    val cartCount: LiveData<Int>
        get() = _cartCount

    private val _cartItems: MutableLiveData<List<OrderProduct>> = MutableLiveData()
    val cartItems: LiveData<List<OrderProduct>>
        get() = _cartItems

    private val _cartCleared: MutableLiveData<Boolean> = MutableLiveData()
    val cartCleared: LiveData<Boolean>
        get() = _cartCleared

    var _liveStock: MutableLiveData<String> = MutableLiveData()
    val liveStock: LiveData<String>
        get() = _liveStock

    var _slideImagesFromFile: MutableLiveData<List<ImageSlide>> = MutableLiveData()
    val slideImagesFromFile: LiveData<List<ImageSlide>>
        get() = _slideImagesFromFile


    fun uploadOrder(discountAmount: Double){
        val orderTransList = orderProductsList.map { product ->
            product?.run {
                toOrderRequestTrans()
            }
        }
        var gTotal = 0F
        orderProductsList.forEach { pdt->
            pdt?.run {
                gTotal += (qty.toInt() * product_price.toFloat())
            }
        }
        val orderMaster = OrderRequestMaster(
            AppPreferences.userId,
            orderShopId,
            order_status = "1",
            (gTotal - discountAmount).toString(),
            orderTransList.size.toString(),
            discountAmount.toString(),
            orderTransList
        )

        viewModelScope.launch {
            orderRepository.uploadOrder(orderMaster).let {
                _orderUpload.value = it is Result.Success && it.data.isError.not()
            }
        }
    }


    fun getAllProducts(){
        viewModelScope.launch {
            productRepository.getAllProductsFromDb().collect {
                if (it is Result.Success){
                    _allProductsList.value = it.data
                }
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

    fun getProductsFromDbByCategory(categoryId: String){
        viewModelScope.launch {
            productRepository.getProductFromDbByCategory(categoryId).collect {
                _productsByCategory.value = it
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

    fun addToCart(product: Product?, quantity: Int){
        product?.run {
            toOrderEntity(quantity)
        }?.let { productToSave ->
            viewModelScope.launch {
                orderRepository.addProductToCart(productToSave).collect {
                    _cartCount.value = it
                }
            }
        }
    }

    fun getCartProductsCount(){
        viewModelScope.launch {
            orderRepository.getCartProductsCount().collect {
                _cartCount.value = it
            }
        }
    }

    fun getAllCartItems(){
        viewModelScope.launch {
            orderRepository.getAllCartItems().let {
                _cartItems.value = it
            }
        }
    }

    fun updateQuantity(id: String, qty: String){
        viewModelScope.launch {
            orderRepository.updateQuantity(id, qty).let {
                orderRepository.getAllCartItems().let {
                    _cartItems.value = it
                }
            }
        }
    }

    fun removeProduct(id: String){
        viewModelScope.launch {
            orderRepository.removeProduct(id).let {
                orderRepository.getAllCartItems().let {
                    _cartItems.value = it
                }
            }
        }
    }

    @DelicateCoroutinesApi
    fun emptyCart(){
        GlobalScope.launch {
            orderRepository.emptyCart()
        }
    }

    fun getProductLiveStock(pBatchId: String){
        val productStockRequest = ProductStockRequest(pBatchId)
        viewModelScope.launch {
            productRepository.getProductLiveStock(productStockRequest).let {
                if (it is Result.Success && it.data.any()){

                    _liveStock.value = it.data[0].stock?:"0"
                }else{
                    _liveStock.value = "0"
                }
            }
        }
    }

    fun getProductImagesFromFile(productId: String, context: Context){
        viewModelScope.launch {
            val dir =  File(context.getExternalFilesDir(null).toString() + "/CapFiles/")
            if (dir.exists()){
                val fileList = dir.listFiles().filter { f ->
                    val name = f.name.split("_").toTypedArray()
                    name[0] == productId
                }
                if (fileList.isNotEmpty()){
                    val imageSlidesFormFile = fileList.map {
                        it.toImageSlide()
                    }
                    _slideImagesFromFile.value = imageSlidesFormFile

                }else{
                    _slideImagesFromFile.value = listOf()
                }
            }else{
                _slideImagesFromFile.value = listOf()
            }
        }
    }


}