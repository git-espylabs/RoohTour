package com.espy.roohtour.api.interceptor

import com.espy.roohtour.api.HttpErrorState
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Common HTTP error interceptor.
 */
class UnauthorisedInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        try {
            when (response.code) {
                HttpErrorState.UNAUTHORIZED.code,
                HttpErrorState.INVALID_SESSION.code,
                HttpErrorState.SESSION_EXPIRED.code -> {

                }
                HttpErrorState.API_OR_SERVER_DOWN.code -> {

                }
                HttpErrorState.INTERNAL_SERVER_ERROR.code -> {

                }
                else -> {
                    /*Do nothing here */
                }
            }
        } catch (ex: Exception) {

        }
        return response
    }
}