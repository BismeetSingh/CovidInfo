package com.bismeet.covidinfo

import com.bismeet.covidinfo.utils.convertStringToTimestampForLastUpdateDate
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TimeUtilUnitTest {
    @Test
    fun dateFormatIsValid(){
        val timestamp= convertStringToTimestampForLastUpdateDate("2020-05-30T03:10:57.728Z")
        assertThat(timestamp,`is`(1590808257728))
    }
}
