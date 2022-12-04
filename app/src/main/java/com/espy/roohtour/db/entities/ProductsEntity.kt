package com.espy.roohtour.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Products")
class ProductsEntity (
    var id: String = "0",
    var category_id: String? = "",
    var product_name: String? = "",
    var description: String? = "",
    var product_code: String? = "",
    var brand_id: String? = "",
    var unit_id: String? = "",
    var price: String? = "",
    var batchcode: String? = "",
    var thumbnail: String? = "",
    var stock: String? = "",
    var batchid: String? = "",
    var mrp: String? = "",
    @PrimaryKey(autoGenerate = true) var row_id: Int = 0
)