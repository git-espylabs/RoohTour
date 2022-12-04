package com.espy.roohtour.api.services

import com.espy.roohtour.api.HttpEndPoints
import com.espy.roohtour.api.models.order.OrderRequestMaster
import com.espy.roohtour.api.models.order.OrderResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApiInterface {

    @POST(HttpEndPoints.CLENSA_ORDER_POST)
    fun uploadOrderAsync(
        @Body orderRequestMaster: OrderRequestMaster
    ): Deferred<OrderResponse>
}