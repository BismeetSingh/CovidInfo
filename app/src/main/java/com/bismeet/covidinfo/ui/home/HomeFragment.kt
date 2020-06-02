package com.bismeet.covidinfo.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bismeet.covidinfo.R
import com.bismeet.covidinfo.data.FilteredCountry
import com.bismeet.covidinfo.database.CountryNamesDB
import com.bismeet.covidinfo.database.WorldDatabase
import com.bismeet.covidinfo.databinding.CardFilteredByCountryLayoutBinding
import com.bismeet.covidinfo.databinding.CardInfoLayoutBinding
import com.bismeet.covidinfo.databinding.FragmentHomeBinding
import com.bismeet.covidinfo.utils.DateAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var cardInfoLayoutBinding: CardInfoLayoutBinding

    //    private val multiSelectDialog = MultiSelectDialog()
    lateinit var countryNameCodeMap: HashMap<String, String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )
        cardInfoLayoutBinding = CardInfoLayoutBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val factory = HomeViewModelFactory(
            WorldDatabase.getInstance(requireContext()).worldSummaryDao,
            application
        )

        homeViewModel =
            ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        fragmentHomeBinding.lifecycleOwner = this
        fragmentHomeBinding.cardInfo.viewmodel = homeViewModel
        fragmentHomeBinding.homeViewModel = homeViewModel
        fragmentHomeBinding.cardInfoCurrentSelection.viewmodel = homeViewModel
        val cardContentBinding = CardFilteredByCountryLayoutBinding.inflate(LayoutInflater.from(context))
        cardContentBinding.lifecycleOwner = this
        homeViewModel.recoveredCountFromCountry.observe(viewLifecycleOwner, Observer {
            Timber.d("%s Cases Recovered", it)
//            fragmentHomeBinding.cardInfoCurrentSelection.recoveredCount.text=it.toString()
        })
        homeViewModel.selectedCountryNameData.observe(viewLifecycleOwner, Observer {
            Timber.d("Selected First %s", it)
        })
        homeViewModel.countryLiveData.observe(viewLifecycleOwner, Observer {
            Timber.d("Selected Country %s", it)
            it?.let {
                fragmentHomeBinding.cardInfoCurrentSelection.root.visibility = View.VISIBLE
                setFilteredCountryData(it)


            }

        })

        homeViewModel.countryNameDataNewUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                showDialogForSelection(it)
            }
//
        })
        homeViewModel.confirmeddataSetLive.observe(viewLifecycleOwner, Observer {

            fragmentHomeBinding.confirmedChart.invalidate()
            fragmentHomeBinding.confirmedChart.refreshDrawableState()
            if (fragmentHomeBinding.confirmedChart.data == null
                || fragmentHomeBinding.confirmedChart.data.getDataSetCount() == 0
            ) {

                fragmentHomeBinding.confirmedChart.data = LineData(it)

//                fragmentHomeBinding.confirmedChart.data=it

            } else {
                val set1 =
                    fragmentHomeBinding.confirmedChart.data.getDataSetByIndex(0) as LineDataSet
                set1.setValues(it.values)
                set1.setDrawValues(false)
                set1.notifyDataSetChanged()
                fragmentHomeBinding.confirmedChart.data.notifyDataChanged()
                fragmentHomeBinding.confirmedChart.notifyDataSetChanged()

            }


        })
        homeViewModel.recoveredDataSetLive.observe(viewLifecycleOwner, Observer {
//            fragmentHomeBinding.recoveredChart.invalidate()
            Timber.d("Recovering")
            fragmentHomeBinding.recoveredChart.invalidate()
            fragmentHomeBinding.recoveredChart.refreshDrawableState()
            if (fragmentHomeBinding.recoveredChart.data == null
                || fragmentHomeBinding.recoveredChart.data.getDataSetCount() == 0
            ) {

                fragmentHomeBinding.recoveredChart.data = LineData(it)

//                fragmentHomeBinding.confirmedChart.data=it

            } else {
                val set1 =
                    fragmentHomeBinding.recoveredChart.data.getDataSetByIndex(0) as LineDataSet
                set1.setValues(it.values)
                set1.setDrawValues(false)
                set1.notifyDataSetChanged()
                fragmentHomeBinding.recoveredChart.data.notifyDataChanged()
                fragmentHomeBinding.recoveredChart.notifyDataSetChanged()

            }


        })
        homeViewModel.deathDataSetLive.observe(viewLifecycleOwner, Observer {
//            fragmentHomeBinding.deathChart.invalidate()
            fragmentHomeBinding.deathChart.invalidate()
            fragmentHomeBinding.deathChart.refreshDrawableState()
            if (fragmentHomeBinding.deathChart.data == null
                || fragmentHomeBinding.deathChart.data.getDataSetCount() == 0
            ) {

                fragmentHomeBinding.deathChart.data = LineData(it)

//                fragmentHomeBinding.confirmedChart.data=it

            } else {
                val set1 =
                    fragmentHomeBinding.deathChart.data.getDataSetByIndex(0) as LineDataSet
                set1.values = it.values
                set1.setDrawValues(false)
                set1.notifyDataSetChanged()
                fragmentHomeBinding.deathChart.data.notifyDataChanged()
                fragmentHomeBinding.deathChart.notifyDataSetChanged()

            }


        })
//
        return fragmentHomeBinding.root
    }

    private fun showDialogForSelection(countriesList: List<CountryNamesDB>) {
        countryNameCodeMap = HashMap()
        countriesList.forEach {
            countryNameCodeMap[it.countryName] = it.code
        }
        countryDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = countryDropDown.selectedItem.toString()
                homeViewModel.onSelectItem(countryNameCodeMap[item]!!)
            }

        }
//
//
    }

    private fun setFilteredCountryData(countryItemList: FilteredCountry) {

        homeViewModel.setDataToCharts(countryItemList)
//
        val xAxisFormatter = DateAxisValueFormatter()
        val xAxis: XAxis = fragmentHomeBinding.confirmedChart.xAxis
        val xAxis1: XAxis = fragmentHomeBinding.deathChart.xAxis
        val xAxisRecovered = fragmentHomeBinding.recoveredChart.xAxis



        xAxis.valueFormatter = xAxisFormatter
        xAxis1.valueFormatter = xAxisFormatter
        xAxisRecovered.valueFormatter = xAxisFormatter

        fragmentHomeBinding.confirmedChart.xAxis.setDrawGridLines(false)
        fragmentHomeBinding.recoveredChart.axisLeft.setDrawGridLines(false)
        fragmentHomeBinding.deathChart.axisLeft.setDrawGridLines(false)
        fragmentHomeBinding.confirmedChart.axisLeft.setDrawGridLines(false)
        fragmentHomeBinding.recoveredChart.axisRight.setDrawGridLines(false)
        fragmentHomeBinding.deathChart.axisRight.setDrawGridLines(false)
        fragmentHomeBinding.confirmedChart.axisRight.setDrawGridLines(false)
        fragmentHomeBinding.confirmedChart.axisLeft.isEnabled = false
        fragmentHomeBinding.recoveredChart.axisLeft.isEnabled = false
        fragmentHomeBinding.deathChart.axisLeft.isEnabled = false
        fragmentHomeBinding.deathChart.xAxis.setDrawGridLines(false)
        fragmentHomeBinding.recoveredChart.xAxis.setDrawGridLines(false)
        fragmentHomeBinding.recoveredChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        fragmentHomeBinding.deathChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        fragmentHomeBinding.confirmedChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        fragmentHomeBinding.confirmedChart.description.isEnabled = false
        fragmentHomeBinding.deathChart.description.isEnabled = false
        fragmentHomeBinding.confirmedChart.xAxis.axisLineColor = Color.RED
        fragmentHomeBinding.confirmedChart.xAxis.textColor = Color.RED
        fragmentHomeBinding.confirmedChart.axisRight.textColor = Color.RED

        fragmentHomeBinding.deathChart.xAxis.axisLineColor =
            ContextCompat.getColor(requireContext(), R.color.deceasedColor)
        fragmentHomeBinding.deathChart.xAxis.textColor =
            ContextCompat.getColor(requireContext(), R.color.deceasedColor)
        fragmentHomeBinding.deathChart.axisRight.textColor =
            ContextCompat.getColor(requireContext(), R.color.deceasedColor)

        fragmentHomeBinding.recoveredChart.xAxis.axisLineColor =
            ContextCompat.getColor(requireContext(), R.color.recoveredColor)
        fragmentHomeBinding.recoveredChart.xAxis.textColor =
            ContextCompat.getColor(requireContext(), R.color.recoveredColor)
        fragmentHomeBinding.recoveredChart.axisRight.textColor =
            ContextCompat.getColor(requireContext(), R.color.recoveredColor)

        fragmentHomeBinding.recoveredChart.description.isEnabled = false
        fragmentHomeBinding.confirmedChart.setDrawMarkers(false)
        fragmentHomeBinding.confirmedChart.highlightValue(null)
        fragmentHomeBinding.deathChart.highlightValue(null)
        fragmentHomeBinding.recoveredChart.highlightValue(null)
        fragmentHomeBinding.confirmedChart.isEnabled = false
        fragmentHomeBinding.deathChart.legend.isEnabled = false
        fragmentHomeBinding.recoveredChart.legend.isEnabled = false


//        fragmentHomeBinding.confirmedChart.


    }


}
