package com.bismeet.covidinfo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class CovidApplication:Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        delayInit()


    }

    private fun delayInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
        }

    }

}