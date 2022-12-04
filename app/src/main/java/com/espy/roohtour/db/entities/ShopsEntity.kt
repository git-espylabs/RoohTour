package com.espy.roohtour.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Shops")
class ShopsEntity (
    var id: String? = "",
    var shop_name: String? = "",
    var lattitude: String? = "",
    var longitude: String? = "",
    var shop_address: String? = "",
    var shop_primary_number: String? = "",
    var shop_secondary_number: String? = "",
    var shop_image: String? = "",
    var shop_oustanding_amount: String? = "",
    var route_id: String? = "",
    var added_by: String? = "",
    var login_id: String? = "",
    var route_name: String? = "",
    @PrimaryKey(autoGenerate = true) var row_id: Int = 0
)