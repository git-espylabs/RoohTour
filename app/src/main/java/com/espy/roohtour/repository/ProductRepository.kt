package com.espy.roohtour.repository

import android.content.Context
import android.util.Log
import com.espy.roohtour.api.RestServiceProvider
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.login.ProductAllStaffRequest
import com.espy.roohtour.api.models.products.*
import com.espy.roohtour.api.services.ImageDownloader
import com.espy.roohtour.db.DatabaseProvider
import com.espy.roohtour.db.entities.ProductsEntity
import com.espy.roohtour.domain.toDomain
import com.espy.roohtour.domain.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepository: BaseRepository() {

    override fun onCleared() {
    }

    private val productsDao = DatabaseProvider.getProductsDao()

    fun getProductListFromServer(ProductAllStaffRequest: ProductAllStaffRequest): Flow<Result<List<Product>>> {
        return flow {
            try {
                val response = RestServiceProvider
                    .getProductService()
                    .getProductsListAsync(ProductAllStaffRequest)
                    .await()

                if (response.data.any()){
                    val prdts = response.data.map { p->
                        p.toEntity()
                    }
                    insertProductsToDb(prdts)

                    productsDao.getProducts().collect {
                        val dbList = it.map { productsEntity ->
                            productsEntity.toDomain()
                        }
                        emit(Result.Success(dbList))
                    }

                }else{
                    emit(Result.Error(Exception("error")))
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getCategoriesList(): Flow<Result<List<Category>>> {
        return flow {
            try {
                val response = RestServiceProvider
                    .getProductService()
                    .getCategoriesListAsync()
                    .await()

                emit(Result.Success(response.data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getProductByCategory(productsByCategoryRequest: ProductsByCategoryRequest): Flow<Result<List<Product>>> {
        return flow {
            try {
                val response = RestServiceProvider
                    .getProductService()
                    .getProductsByCategoryAsync(productsByCategoryRequest)
                    .await()

                emit(Result.Success(response.data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getProductFromDbByCategory(categoryId: String): Flow<Result<List<Product>>> {
        return flow {
            try {
                productsDao.getProductsByCategory(categoryId).collect {
                    val dbList = it.map { productsEntity ->
                        productsEntity.toDomain()
                    }
                    emit(Result.Success(dbList))
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getProductImages(productImageRequest: ProductImageRequest): Flow<Result<List<ProductImage>>> {
        return flow {
            try {
                val response = RestServiceProvider
                    .getProductService()
                    .getProductImagesAsync(productImageRequest)
                    .await()

                emit(Result.Success(response.data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun insertProductsToDb(list: List<ProductsEntity>){
        productsDao.saveProducts(list)
    }

    fun getAllProductsFromDb(): Flow<Result<List<Product>>>{
        return flow{
            try {
                productsDao.getProducts().collect {
                    val dbList = it.map { productsEntity ->
                        productsEntity.toDomain()
                    }
                    emit(Result.Success(dbList))
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProductLiveStock(productStockRequest: ProductStockRequest): Result<List<ProductStock>> {
        return try {
            val response = RestServiceProvider
                .getProductService()
                .getProductLiveStockAsync(productStockRequest)
                .await()

            Result.Success(response.data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun syncProductImages(): Result<List<ProductImageData>> {
        return try {
            val response = RestServiceProvider
                .getProductService()
                .getProductsImagesAsync()
                .await()

            Result.Success(response.data)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun downloadImages(context: Context, url: String, fileName: String, storageName: String, totalFiles: Int, index: Int): Flow<Int>{
        val a = ImageDownloader().downloadImageAsync(context, url, fileName, storageName).await()
        val percDec = index.toFloat()/totalFiles.toFloat()
        val perc = percDec * 100F

        Log.e("RES","--> " + a + ": "+ perc.toInt().toString())
        return flow {
            if (a == "1") {
                emit(perc.toInt())
            } else {
                emit(-1)
            }
        }
    }
}