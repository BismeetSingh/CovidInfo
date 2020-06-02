package com.bismeet.covidinfo.utils

import android.content.Context
import android.content.Intent

fun Context.makeIntent(classFile:Class<out Any>){
    val intent= Intent(this,classFile)
    startActivity(intent)

}