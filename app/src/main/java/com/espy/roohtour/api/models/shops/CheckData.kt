package com.espy.roohtour.api.models.shops

import kotlinx.serialization.SerialName

object CheckData {
    data class CategoryNames(
        @SerialName("id")
        var id: String,
        @SerialName("category_name")
        var route_name: String
    ) {
        override fun toString(): String {
            return route_name
        }
    }

    var categoryList = arrayListOf(

        CategoryNames("0", "Select Category"),
        CategoryNames("1", "A"),
        CategoryNames("2", "B"),
        CategoryNames("3", "C"),
        CategoryNames("4", "Direct Customer"),

        )




}
