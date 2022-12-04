package com.espy.roohtour.api

/**
 * HTTP error states.
 */
enum class HttpErrorState(val code: Int) {
    UNAUTHORIZED(401),
    INVALID_SESSION(402),
    SESSION_EXPIRED(403),
    API_OR_SERVER_DOWN(404),
    INTERNAL_SERVER_ERROR(500),
}