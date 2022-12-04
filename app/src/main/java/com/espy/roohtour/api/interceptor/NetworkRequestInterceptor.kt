package com.espy.roohtour.api.interceptor

import com.espy.roohtour.api.header.NetworkRequestHeader
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Class used to intercept the request.
 */
class NetworkRequestInterceptor(private val networkHeaders: List<NetworkRequestHeader>) :
    Interceptor {

    @Throws(IOException::class)
    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        networkHeaders.forEach {
            requestBuilder.addHeader(it.key, it.value)
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}