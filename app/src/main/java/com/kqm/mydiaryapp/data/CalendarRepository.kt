package com.kqm.mydiaryapp.data

import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.framework.CalendarDataSourceImpl

class CalendarRepository(
   private val calendarDataSource: CalendarDataSourceImpl
) {
    fun getCalendar(): Pair<List<Year>, Int> = calendarDataSource.getRangeDates()
}