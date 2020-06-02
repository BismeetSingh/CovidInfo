package com.bismeet.covidinfo.utils

import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bismeet.covidinfo.data.CountryNames
import com.bismeet.covidinfo.database.CountryNamesDB
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import timber.log.Timber


@BindingAdapter("spinnerlistData")
fun bindSpinner(countrySpinner: SearchableSpinner, data: List<CountryNamesDB>?) {
    val listCountryNames = arrayListOf<String>()
    data?.let {
        for (country in data) {
            listCountryNames.add(country.countryName)
        }
    }

    val adapter =
        ArrayAdapter(
            countrySpinner.context,
            android.R.layout.simple_spinner_dropdown_item,
            listCountryNames
        )
    countrySpinner.adapter = adapter
}
@BindingAdapter("setcountryCount")
fun bindTextViewCount(countryCount:TextView,data:Int){


        countryCount.text=data.toString()
        Timber.d(data.toString())
//

}

    @BindingAdapter("setCountryName")
fun setCountryNameToTextView(countryCount:TextView,countryName:String?){
    countryName?.let {
        countryCount.text=countryName
    }
}