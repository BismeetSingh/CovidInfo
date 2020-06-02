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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "world_data")
data class WorldData(
        @PrimaryKey(autoGenerate = true)
        var summaryId: Long = 0L,

        @ColumnInfo(name = "confirmed_cases")
        val confirmedCases:Int,

        @ColumnInfo(name = "deaths")
        var deaths:Int,

        @ColumnInfo(name = "recovered_cases")
        var recoveredCases: Int,

        @ColumnInfo(name = "date")
        var date:String
)