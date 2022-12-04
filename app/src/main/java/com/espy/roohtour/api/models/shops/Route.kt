package com.espy.roohtour.api.models.shops

import kotlinx.serialization.SerialName

data class Route(
    @SerialName("id")
    var id: String,
    @SerialName("route_name")
    var route_name: String
){
    override fun toString(): String {
        return route_name
    }
}
