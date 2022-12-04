package com.espy.roohtour.ui.sync.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.login.ProductAllStaffRequest
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.api.models.products.ProductImageData
import com.espy.roohtour.api.models.shops.Shop
import com.espy.roohtour.app.AppSettings
import com.espy.roohtour.repository.ProductRepository
import com.espy.roohtour.repository.ShopRepository
import com.espy.roohtour.repository.SyncRepository
import com.espy.roohtour.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class SyncViewModel : BaseViewModel() {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private val downloadScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private val syncRepository = SyncRepository()
    private val productRepository = ProductRepository()
    private val shopRepositoryRepository = ShopRepository()

    var _productsList: MutableLiveData<Result<List<Product>>> = MutableLiveData()
    val productsList: LiveData<Result<List<Product>>>
        get() = _productsList

    var _shopsList: MutableLiveData<Result<List<Shop>>> = MutableLiveData()
    val shopsList: LiveData<Result<List<Shop>>>
        get() = _shopsList

    var _productsCount: MutableLiveData<Int> = MutableLiveData()
    val productsCount: LiveData<Int>
        get() = _productsCount

    var _shopsCount: MutableLiveData<Int> = MutableLiveData()
    val shopsCount: LiveData<Int>
        get() = _shopsCount

    var _productImagesDataList: MutableLiveData<Result<List<ProductImageData>>> = MutableLiveData()
    val productImagesDataList: LiveData<Result<List<ProductImageData>>>
        get() = _productImagesDataList

    var _imageDownloadPer: MutableLiveData<Triple<Int, Int, Int>> = MutableLiveData()
    val imageDownloadPer: LiveData<Triple<Int, Int, Int>>
        get() = _imageDownloadPer

    var _pdtImagesCount: MutableLiveData<Int> = MutableLiveData()
    val pdtImagesCount: LiveData<Int>
        get() = _pdtImagesCount



    fun getProductsList(userId: String) {
        viewModelScope.launch {
            var ProductAllStaffRequest= ProductAllStaffRequest(userId)
            productRepository.getProductListFromServer(ProductAllStaffRequest).collect {
                _productsList.value = it
            }
        }
    }

    fun getProductsCount(){
        viewModelScope.launch {
            productRepository.getAllProductsFromDb().collect {
                if (it is Result.Success){
                    _productsCount.value = it.data.size
                }
            }
        }
    }

    fun getShopsList(){
        viewModelScope.launch {
            shopRepositoryRepository.getShopsListFromServer().collect {
                _shopsList.value = it
            }
        }
    }

    fun getShopsCount(){
        viewModelScope.launch {
            shopRepositoryRepository.getAllShopsFromDb().collect {
                if (it is Result.Success){
                    _shopsCount.value = it.data.size
                }
            }
        }
    }

    fun syncProductImages(){
        viewModelScope.launch {
            productRepository.syncProductImages().let {
                _productImagesDataList.value = it
            }
        }
    }

    fun downloadImages(context: Context, list: List<ProductImageData>){
        val job = downloadScope.launch {
            val dir = File(context.getExternalFilesDir(null).toString() + "/CapFiles/")
            if (dir.exists()) {
                org.apache.commons.io.FileUtils.deleteDirectory(dir)
            }
            list.forEachIndexed { indx, imgData ->
                imgData.image?.let {
                    productRepository.downloadImages(context, AppSettings.endPoints.IMAGE_ASSETS, it, imgData.product_id?:"00", list.size, indx + 1).collect {
                        _imageDownloadPer.value = Triple(it, list.size, indx+1)
                        if (it == -1){
                            this.cancel()
                        }
                    }
                }
            }

        }
    }

    fun getProductImagesCount(context: Context){
        viewModelScope.launch {
            val dir = File(context.getExternalFilesDir(null).toString() + "/CapFiles/")
            if (dir.exists()){
                _pdtImagesCount.value = dir.listFiles().size
            }else{
                _pdtImagesCount.value = 0
            }
        }
    }

}