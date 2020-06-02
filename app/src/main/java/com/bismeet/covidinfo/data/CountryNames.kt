package com.bismeet.covidinfo.data

import com.bismeet.covidinfo.database.CountryNamesDB

data class CountryNames(
    val _cacheHit: Boolean,
    val `data`: List<Data>

){
    fun getCountryNamesForDB(): ArrayList<CountryNamesDB> {
        val countryList= arrayListOf<CountryNamesDB>()

        data.forEach {
            countryList.add(CountryNamesDB(it.code,it.name))
        }
        return countryList

    }
}


data class Data(
    val code: String,
    val coordinates: Coordinates,
    val latest_data: LatestData,
    val name: String,
    val population: Int,
    val today: Today,
    val updated_at: String
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class LatestData(
    val calculated: Calculated,
    val confirmed: Int,
    val critical: Int,
    val deaths: Int,
    val recovered: Int
)

data class Today(
    val confirmed: Int,
    val deaths: Int
)

data class Calculated(
    val cases_per_million_population: Double,
    val death_rate: Double,
    val recovered_vs_death_ratio: Any,
    val recovery_rate: Double
)