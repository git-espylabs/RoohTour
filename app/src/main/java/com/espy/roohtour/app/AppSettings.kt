package com.espy.roohtour.app

import com.espy.roohtour.api.HttpEndPoints
import com.espy.roohtour.api.header.NetworkRequestHeader

internal class AppSettings {

    companion object{
        const val APP_PREF = "com.espy.roohtour"

        const val CACHED_DB_PATH = "database/roohtour_config_db.db"
        const val DB_NAME = "roohtourapp.db"

        const val NETWORK_READ_TIME_OUT = 30 * 1000
        const val NETWORK_CONNECTION_TIME_OUT = 10 * 1000

        const val CLENSA_FILE_PROVIDER = "fileprovider"

        val endPoints = HttpEndPoints

        val cacheControl = NetworkRequestHeader("Cache-Control", "no-cache")
        val contentType = NetworkRequestHeader("Content-Type", "application/json")
    }
}