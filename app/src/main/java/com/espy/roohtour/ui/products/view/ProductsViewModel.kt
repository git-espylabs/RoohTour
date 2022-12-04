package com.espy.roohtour.ui.products.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.products.ProductImageRequest
import com.espy.roohtour.api.models.products.ProductStockRequest
import com.espy.roohtour.domain.toImageSlide
import com.espy.roohtour.repository.ProductRepository
import com.espy.roohtour.ui.base.BaseViewModel
import com.espy.roohtour.ui.products.models.ImageSlide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class ProductsViewModel : BaseViewModel() {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    var productId = ""
    private val productRepository = ProductRepository()

    var _slideImages: MutableLiveData<List<ImageSlide>> = MutableLiveData()
    val slideImages: LiveData<List<ImageSlide>>
        get() = _slideImages

    var _liveStock: MutableLiveData<String> = MutableLiveData()
    val liveStock: LiveData<String>
        get() = _liveStock

    var _slideImagesFromFile: MutableLiveData<List<ImageSlide>> = MutableLiveData()
    val slideImagesFromFile: LiveData<List<ImageSlide>>
        get() = _slideImagesFromFile



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
}