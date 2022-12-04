package com.espy.roohtour.db

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.espy.roohtour.app.AppSettings

internal class DatabaseProvider {

    fun initDb(context: Context) {
        try {
            instance = Room.databaseBuilder(
                context.applicationContext,
                CapDatabase::class.java,
                AppSettings.DB_NAME
            )
                .addMigrations(MIGRATION_1_2)
                .createFromAsset(AppSettings.CACHED_DB_PATH).allowMainThreadQueries().build()

        } catch (e: Exception) {
            throw e
        }
    }

    internal fun clearAllTables() {
        try {
            getDBLocked { it.clearAllTables() }
        } catch (e: Exception) {
            throw e
        }
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Products ADD COLUMN mrp TEXT")
        }
    }

    companion object {

        private lateinit var instance: CapDatabase

        internal fun <T> getDBLocked(block: (db: CapDatabase) -> T) {
            synchronized(this) {
                block(instance)
            }
        }

        internal fun getProductsDao() = instance.productsDao
        internal fun getShopsDao() = instance.shopsDao
        internal fun getOrder() = instance.orderDao

    }
}