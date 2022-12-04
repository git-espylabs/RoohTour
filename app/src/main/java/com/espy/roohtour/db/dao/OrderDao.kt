package com.espy.roohtour.db.dao

import androidx.room.*
import com.espy.roohtour.db.entities.OrderEntity

@Dao
abstract class OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProduct(product: OrderEntity): Long?

    @Query("DELETE from Cart where productId= :id")
    abstract fun removeProduct(id: String)

    @Query("DELETE from Cart")
    abstract fun emptyCart()

    @Query("SELECT COUNT(*) from Cart")
    abstract fun getCartProductsCount(): Int?

    @Query("SELECT * from Cart")
    abstract fun getCartProducts(): List<OrderEntity>

    @Query("UPDATE Cart SET productQty = :qty where productId= :id")
    abstract fun updateQuantity(id: String, qty: String)
}