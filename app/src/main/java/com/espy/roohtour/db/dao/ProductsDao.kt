package com.espy.roohtour.db.dao

import androidx.room.*
import com.espy.roohtour.db.entities.ProductsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductsDao {

    @Transaction
    open fun saveProducts(products: List<ProductsEntity>) {
        deleteAllProducts()
        insertProducts(products)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProducts(products: List<ProductsEntity>)

    @Query("DELETE FROM Products")
    abstract fun deleteAllProducts()

    @Query("SELECT * FROM Products")
    abstract fun getProducts(): Flow<List<ProductsEntity>>

    @Query("SELECT * FROM Products WHERE category_id = :cid")
    abstract fun getProductsByCategory(cid: String): Flow<List<ProductsEntity>>
}