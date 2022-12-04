package com.espy.roohtour.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "Cart", indices = [Index(value = ["productId"], unique = true)])
class OrderEntity (
    @PrimaryKey var productId: String,
    var productName: String,
    var productBatchId: String,
    var productPrice: String,
    var productQty: String,
    var productSum: String
)