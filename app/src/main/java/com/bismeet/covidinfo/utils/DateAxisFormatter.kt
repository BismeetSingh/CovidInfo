package com.bismeet.covidinfo.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import timber.log.Timber
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DateAxisValueFormatter    (// minimum timestamp in your data set
) :
    ValueFormatter() {


    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val convertedTimestamp = value.toLong()
        Timber.d("%d",convertedTimestamp)

        val requiredFormat = SimpleDateFormat("dd-MMM", Locale.US)
        val timestamp= Timestamp(convertedTimestamp)
        val newDate=Date(timestamp.time)

        Timber.d(requiredFormat.format(newDate))
//        Timber.d(converLongtoString(value.toLong()))
        return requiredFormat.format(newDate)
    }

}