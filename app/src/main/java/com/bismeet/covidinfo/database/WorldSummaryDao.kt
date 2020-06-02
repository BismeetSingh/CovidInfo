/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bismeet.covidinfo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WorldSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(night: WorldData): Long

    @Query("SELECT * FROM world_data ORDER BY confirmed_cases desc")
    fun getEntireData(): LiveData<List<WorldData>>

    @Query("SELECT * from world_data ORDER BY summaryId  ASC LIMIT 1")
    fun getFirstRecord(): LiveData<WorldData>

    @Query("SELECT * FROM countries")
    fun getCountryNames(): LiveData<List<CountryNamesDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countryNamesDBList: List<CountryNamesDB>)


}