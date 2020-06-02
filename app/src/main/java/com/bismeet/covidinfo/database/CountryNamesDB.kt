package com.bismeet.covidinfo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryNamesDB(
    @PrimaryKey
    val code: String,
    @ColumnInfo(name = "country_name")
    val countryName:String
)