package com.tutorial.tvshowsapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen: SplashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{ true }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}