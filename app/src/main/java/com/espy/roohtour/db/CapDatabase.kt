package com.espy.roohtour.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.espy.roohtour.db.dao.OrderDao
import com.espy.roohtour.db.dao.ProductsDao
import com.espy.roohtour.db.dao.ShopsDao
import com.espy.roohtour.db.entities.OrderEntity
import com.espy.roohtour.db.entities.ProductsEntity
import com.espy.roohtour.db.entities.ShopsEntity

@Database(
    version = 2,
    exportSchema = false,
    entities = [
        OrderEntity::class,
        ProductsEntity::class,
        ShopsEntity::class,
    ]
)

abstract class CapDatabase : RoomDatabase()  {
    abstract val orderDao: OrderDao
    abstract val productsDao: ProductsDao
    abstract val shopsDao: ShopsDao
}