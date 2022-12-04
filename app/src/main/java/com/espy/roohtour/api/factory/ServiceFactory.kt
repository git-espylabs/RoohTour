package com.espy.roohtour.api.factory

import com.espy.roohtour.api.Environment
import com.espy.roohtour.api.interceptor.Interceptor
import com.espy.roohtour.app.AppSettings
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Represent the service factory.
 */
abstract class ServiceFactory(
    private val converterFactory: Converter.Factory,
    private val callAdapterFactory: CallAdapter.Factory,
    private val logLevel: HttpLoggingInterceptor.Level,
    private val environment: Environment
) : Interceptor {

    fun <T> create(serviceType: Class<T>): T {
        return create(
            serviceType,
            getHttpClient(),
            converterFactory,
            callAdapterFactory,
            environment.baseUrl
        )
    }

    private fun getNetAdapter(
        client: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory,
        baseUrl: String
    ): Retrofit {
        val builder = Retrofit.Builder()
            .client(client)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .baseUrl(baseUrl)
        return builder.build()
    }

    private fun <T> create(
        serviceType: Class<T>,
        client: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory,
        baseUrl: String
    ): T {
        val retrofit = getNetAdapter(client, converterFactory, callAdapterFactory, baseUrl)
        return retrofit.create(serviceType)
    }

    private fun getHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        interceptors().forEach { interceptor -> builder.addInterceptor(interceptor) }
        builder.addInterceptor(HttpLoggingInterceptor().apply { level = logLevel })
            .connectTimeout(AppSettings.NETWORK_CONNECTION_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(AppSettings.NETWORK_READ_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(AppSettings.NETWORK_READ_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
        return builder.build()
    }
}