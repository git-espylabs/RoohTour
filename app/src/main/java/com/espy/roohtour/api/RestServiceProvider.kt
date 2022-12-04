package com.espy.roohtour.api

import com.espy.roohtour.api.factory.NetworkServiceFactory
import com.espy.roohtour.api.services.*
import com.espy.roohtour.app.AppSettings
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

object RestServiceProvider {

    private val capServiceFactory: NetworkServiceFactory

    private var logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

    private val callAdapterFactory = CoroutineCallAdapterFactory()

    private val gsonConverterFactory: GsonConverterFactory = GsonConverterFactory.create(
        GsonBuilder()
            .setLenient()
            .create()
    )

    init {
        capServiceFactory = initProvider(AppSettings.endPoints.WS_URL_PRODUCTION)
    }

    private fun initProvider(url: String): NetworkServiceFactory {
        return NetworkServiceFactory(
            gsonConverterFactory,
            callAdapterFactory,
            logLevel,
            Environment(url)
        )
    }

    fun getCapService() = capServiceFactory.create(CommonApiInterface::class.java)

    fun getShopService() = capServiceFactory.create(ShopsApiInterface::class.java)

    fun getProductService() = capServiceFactory.create(ProductApiInterface::class.java)

    fun getOrderService() = capServiceFactory.create(OrderApiInterface::class.java)

    fun getAttendanceService() = capServiceFactory.create(AttendanceApiInterface::class.java)
}