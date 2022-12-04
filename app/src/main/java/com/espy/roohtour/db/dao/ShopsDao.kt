package com.espy.roohtour.db.dao

import androidx.room.*
import com.espy.roohtour.db.entities.ShopsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ShopsDao {

    @Transaction
    open fun saveShops(shops: List<ShopsEntity>) {
        deleteAllShops()
        insertShops(shops)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertShops(shops: List<ShopsEntity>)

    @Query("DELETE FROM Shops")
    abstract fun deleteAllShops()

    @Query("SELECT * FROM Shops")
    abstract fun getShops(): Flow<List<ShopsEntity>>
}