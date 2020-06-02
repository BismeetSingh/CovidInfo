package com.bismeet.covidinfo.data



data class FilteredCountry(
    val _cacheHit: Boolean,
    val `data`: DataItem
)
data class DataItem(
    val code: String,
    val coordinates: Coordinates,
    val latest_data: LatestData,
    val name: String,
    val population: Int,
    val timeline: List<Timeline>,
    val today: Today,
    val updated_at: String
)



data class Timeline(
    val active: Int,
    val confirmed: Int,
    val date: String,
    val deaths: Int,
    val is_in_progress: Boolean,
    val new_confirmed: Int,
    val new_deaths: Int,
    val new_recovered: Int,
    val recovered: Int,
    val updated_at: String
)


