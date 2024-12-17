package com.kqm.mydiaryapp.data

import com.kqm.mydiaryapp.domain.Year
import java.time.LocalDate

interface CalendarDataSource {
    fun getRangeDates(centerYear: Int = LocalDate.now().year): List<Year>
}