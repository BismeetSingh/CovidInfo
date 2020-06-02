package com.bismeet.covidinfo.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bismeet.covidinfo.data.CountryNames
import com.bismeet.covidinfo.data.FilteredCountry
import com.bismeet.covidinfo.data.WorldSummary
import com.bismeet.covidinfo.database.CountryNamesDB
import com.bismeet.covidinfo.database.WorldData
import com.bismeet.covidinfo.database.WorldSummaryDao
import com.bismeet.covidinfo.network.CovidNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeRepository(val dataSource: WorldSummaryDao) {

//    private val worldDatabase= WorldDatabase.getInstance()
    val countryNameDataNewUrl:LiveData<List<CountryNamesDB>> = dataSource.getCountryNames()


    val countryLiveData=MutableLiveData<FilteredCountry>()


    val worldSummaryLiveData :LiveData<WorldData> = dataSource.getFirstRecord()


    suspend fun getWorldSummary() {
        withContext(Dispatchers.IO) {
            val countrySummary = CovidNetwork.networkService.getWorldSummaryAsync().await()
            dataSource.insert(countrySummary.getDomainData())

        }
    }

    suspend fun getCountryWiseData(countryName: String) {
        withContext(Dispatchers.IO) {
            val countryFilteredData = CovidNetwork.networkService.getCountryDataFilteredAsync(countryName).await()
            countryLiveData.postValue(countryFilteredData)


//            countryWiseLiveData.postValue(countryFilteredData)
        }
    }

    suspend fun countryNamesNewUrl(){
        withContext(Dispatchers.IO){
            val listNames=CovidNetwork.networkService.getCountryListAsyncNewUrl().await()
//            Timber.d(listNames.toString())
            dataSource.insertCountries(listNames.getCountryNamesForDB())



        }
    }

}