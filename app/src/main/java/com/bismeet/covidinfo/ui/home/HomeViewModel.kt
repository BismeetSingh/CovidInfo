package com.bismeet.covidinfo.ui.home

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bismeet.covidinfo.data.FilteredCountry
import com.bismeet.covidinfo.database.WorldSummaryDao
import com.bismeet.covidinfo.repository.HomeRepository
import com.bismeet.covidinfo.utils.convertStringToTimestampForCaseDate
import com.bismeet.covidinfo.utils.convertStringToTimestampForLastUpdateDate
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel(
    dataSource: WorldSummaryDao,
    val application: Application
) : ViewModel() {
    private val homeRepository = HomeRepository(dataSource)
    val worldSummaryLiveData = homeRepository.worldSummaryLiveData
    val countryLiveData=homeRepository.countryLiveData
    val selectedCountryNameData:LiveData<String>
    get()=Transformations.map(countryLiveData){
        it?.data?.name
    }
    val confirmedCountFromCountry:LiveData<Int>
    get() = Transformations.map(countryLiveData){
        it?.data?.latest_data?.confirmed
    }
    val recoveredCountFromCountry:LiveData<Int>
        get() = Transformations.map(countryLiveData){
            it?.data?.latest_data?.recovered
        }
    val deathCountFromCountry:LiveData<Int>
        get() = Transformations.map(countryLiveData){
            it?.data?.latest_data?.deaths

        }
    val countryNameDataNewUrl=homeRepository.countryNameDataNewUrl

    private val confirmedLiveData=MutableLiveData<LineDataSet>()
    val confirmeddataSetLive:LiveData<LineDataSet>
    get() = confirmedLiveData
    private val recoveredLiveData=MutableLiveData<LineDataSet>()
    val recoveredDataSetLive:LiveData<LineDataSet>
    get() = recoveredLiveData
    private val deathLiveData=MutableLiveData<LineDataSet>()
    val deathDataSetLive:LiveData<LineDataSet> get() = deathLiveData

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        getWorldStatistics()
        getCountryFilteredStatistics("IN")
        getCountryNames()

    }
    private fun getCountryFilteredStatistics(countryName:String){

        viewModelScope.launch {
            try {
                homeRepository.getCountryWiseData(countryName)

            }
            catch (e: Exception) {
                Timber.d(e)


            }
        }
    }
    private fun getCountryNames(){
        viewModelScope.launch {
            try {
                homeRepository.countryNamesNewUrl()

            }catch (e:java.lang.Exception){
                Timber.d(e)
            }
        }
    }


    private fun getWorldStatistics() {
        viewModelScope.launch {
            try {
                homeRepository.getWorldSummary()
//                worldSummaryLiveData.value.Global.NewConfirmed

            } catch (e: Exception) {
                Timber.d(e)


            }


        }
    }
    fun onSelectItem(
         selectedCountry:String
    ) {
        Timber.d(selectedCountry)
        getCountryFilteredStatistics(selectedCountry)

    }
    fun setDataToCharts(countryItemList: FilteredCountry){


            val firstEntryTimestamp = convertStringToTimestampForLastUpdateDate(countryItemList.data.updated_at)
            Timber.d("%d", firstEntryTimestamp?.toInt())
            val confirmedValues: ArrayList<Entry> = ArrayList()
            val deathValues: ArrayList<Entry> = ArrayList()
            val recoveredValues: ArrayList<Entry> = ArrayList()

            for (i in countryItemList.data.timeline) {
                val confirmedDate = i.date
                val a = convertStringToTimestampForCaseDate(confirmedDate)
                val confirmedCountOnDate = i.confirmed

                val deathCountOnDate = i.deaths

                val recoveredCountOnDate = i.recovered


                val confirmedWorldEntry = Entry(a!!.toFloat(), confirmedCountOnDate.toFloat())
                val deathWorldEntry = Entry(a.toFloat(), deathCountOnDate.toFloat())
                val recoveredWorldEntry = Entry(a.toFloat(), recoveredCountOnDate.toFloat())

                confirmedValues.add(confirmedWorldEntry)
                deathValues.add(deathWorldEntry)
                recoveredValues.add(recoveredWorldEntry)

            }
        Collections.sort(confirmedValues, EntryXComparator())

        Collections.sort(deathValues, EntryXComparator())
        Collections.sort(recoveredValues, EntryXComparator())


            val confirmeddataSet = LineDataSet(confirmedValues as List<Entry>?, "Confirmed Cases")
            val deathDataSet = LineDataSet(deathValues as List<Entry>?, "Deaths")
            val recoveredDataSet = LineDataSet(recoveredValues as List<Entry>?, "Recoveries")
            recoveredDataSet.setDrawHorizontalHighlightIndicator(false)
            confirmeddataSet.setDrawHorizontalHighlightIndicator(false)
            deathDataSet.setDrawHorizontalHighlightIndicator(false)
            confirmeddataSet.setDrawFilled(false)
            deathDataSet.setDrawFilled(false)
            recoveredDataSet.setDrawFilled(false)

//            recoveredDataSet.set
//            confir,
            setCirclesForCharts(deathDataSet, recoveredDataSet, confirmeddataSet)
            deathDataSet.color = Color.GRAY
            confirmeddataSet.color = Color.RED
            recoveredDataSet.color = Color.GREEN
            deathDataSet.lineWidth = 2.0f
            confirmeddataSet.lineWidth = 2.0f
            recoveredDataSet.lineWidth = 2.0f
            viewModelScope.launch {
                showChartonUI(confirmeddataSet, recoveredDataSet, deathDataSet)


            }


//

    }

    private fun setCirclesForCharts(
        deathDataSet: LineDataSet,
        recoveredDataSet: LineDataSet,
        confirmeddataSet: LineDataSet
    ) {
        deathDataSet.setDrawCircles(false)
        recoveredDataSet.setDrawCircles(false)
        confirmeddataSet.setDrawCircles(false)
    }
    private suspend fun showChartonUI(
        confirmedData: LineDataSet,
        recoveredData: LineDataSet,
        deathData: LineDataSet
    )
    {
        withContext(Dispatchers.IO){
            confirmedLiveData.postValue(confirmedData)
            recoveredLiveData.postValue(recoveredData)
            deathLiveData.postValue(deathData)
        }

    }

}