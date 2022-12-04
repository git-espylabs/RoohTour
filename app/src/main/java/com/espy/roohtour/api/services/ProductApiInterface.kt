package com.espy.roohtour.api.services

import com.espy.roohtour.api.HttpEndPoints
import com.espy.roohtour.api.models.login.ProductAllStaffRequest
import com.espy.roohtour.api.models.products.*
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApiInterface {

    @POST(HttpEndPoints.CLENSA_PRODUCTS_LIST)
    fun getProductsListAsync(@Body ProductAllStaffRequest: ProductAllStaffRequest): Deferred<Products>

    @GET(HttpEndPoints.CLENSA_CATEGORIES_LIST)
    fun getCategoriesListAsync(): Deferred<Categories>

    @POST(HttpEndPoints.CLENSA_PRODUCTS_BY_CATEGORY)
    fun getProductsByCategoryAsync(
        @Body productsByCategoryRequest: ProductsByCategoryRequest
    ): Deferred<Products>

    @POST(HttpEndPoints.CLENSA_PRODUCT_IMAGES)
    fun getProductImagesAsync(
        @Body productImageRequest: ProductImageRequest
    ): Deferred<ProductImageResponse>

    @POST(HttpEndPoints.CLENSA_PRODUCT_LIVE_STOCK)
    fun getProductLiveStockAsync(
        @Body productStockRequest: ProductStockRequest
    ): Deferred<ProductStockResponse>

    @GET(HttpEndPoints.CLENSA_PRODUCT_IMAGE_SYNC)
    fun getProductsImagesAsync(): Deferred<ProductsImagesData>
}