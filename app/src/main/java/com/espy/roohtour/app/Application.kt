package com.espy.roohtour.app

import android.app.Application
import android.content.Context
import com.espy.roohtour.app.AppSettings.Companion.APP_PREF
import com.espy.roohtour.db.DatabaseProvider
import com.espy.roohtour.preference.PreferenceProvider

private lateinit var appContext: Context

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        DatabaseProvider().initDb(appContext)
        PreferenceProvider.init(appContext, APP_PREF)
    }
}