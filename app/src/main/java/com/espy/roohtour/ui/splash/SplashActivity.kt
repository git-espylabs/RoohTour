package com.espy.roohtour.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.espy.roohtour.R
import com.espy.roohtour.extensions.launchActivity
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.ui.home.HomeActivity
import com.espy.roohtour.ui.profile.view.LoginActivity


class SplashActivity : AppCompatActivity() {

    private val DELAY = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            if (AppPreferences.userId.isNotEmpty()) {
                launchActivity<HomeActivity>()
                finish()
            } else {
                launchActivity<LoginActivity>()
                finish()
            }
        }, DELAY)

    }
}