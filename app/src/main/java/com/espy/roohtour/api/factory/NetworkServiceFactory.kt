package com.espy.roohtour.api.factory

import com.espy.roohtour.api.Environment
import com.espy.roohtour.api.interceptor.NetworkRequestInterceptor
import com.espy.roohtour.api.interceptor.UnauthorisedInterceptor
import com.espy.roohtour.app.AppSettings
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter

class NetworkServiceFactory(
    converterFactory: Converter.Factory,
    callAdapterFactory: CallAdapter.Factory,
    logLevel: HttpLoggingInterceptor.Level,
    environment: Environment,
) : ServiceFactory(converterFactory, callAdapterFactory, logLevel, environment) {

    override fun interceptors(): List<Interceptor> {
        return arrayListOf(
            NetworkRequestInterceptor(
                listOf(
                    AppSettings.cacheControl,
                    AppSettings.contentType
                )
            ),
            UnauthorisedInterceptor()
        )
    }
}