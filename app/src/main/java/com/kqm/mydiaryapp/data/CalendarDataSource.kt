package com.kqm.mydiaryapp.data

import com.kqm.mydiaryapp.domain.Year

interface CalendarDataSource {
    fun getRangeDates(): Pair<List<Year>, Int>
}