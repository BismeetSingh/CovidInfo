package com.bismeet.covidinfo.ui.home

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bismeet.covidinfo.MainActivity
import com.bismeet.covidinfo.R
import com.bismeet.covidinfo.utils.makeIntent

class SplashActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            makeIntent(MainActivity::class.java)
            finish()
        }, 3000)
    }
}