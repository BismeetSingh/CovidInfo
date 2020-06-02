package com.bismeet.covidinfo.utils

import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Throws(ParseException::class)
fun getNumberFormatted(value: Int?): Int {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(value).toInt()
}

fun convertStringToTimestampForLastUpdateDate(strDate: String?): Long? {

    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
    // you can change format of date
    val date = formatter.parse(strDate!!)
//    Timber.d("%d", date.time)
    return date?.time


}

fun convertStringToTimestampForCaseDate(strDate: String?): Long? {

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US);
    // you can change format of date
    val date = formatter.parse(strDate!!)
//    Timber.d("%d", date.time)
    return date?.time


}
