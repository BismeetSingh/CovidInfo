package com.bismeet.covidinfo.data

data class WorldSummary(
    val _cacheHit: Boolean,
    val `data`: List<WorldData>

){
    fun getDomainData()=com.bismeet.covidinfo.database.WorldData(confirmedCases = data[0].confirmed
    ,deaths = data[0].deaths,recoveredCases = data[0].recovered,date = data[0].date)
}

data class WorldData(
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
