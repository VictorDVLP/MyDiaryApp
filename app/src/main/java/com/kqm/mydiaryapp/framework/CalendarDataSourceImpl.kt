package com.kqm.mydiaryapp.framework

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale as ILocale
import com.kqm.mydiaryapp.data.CalendarDataSource
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Month
import com.kqm.mydiaryapp.domain.Year
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

const val YEAR_AGO = 1
const val YEAR_NEXT = 5

class CalendarDataSourceImpl : CalendarDataSource {

    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale("es", "ES"))

    override fun getRangeDates(): Pair<List<Year>, Int> {
        val now = LocalDate.now()
        val years = (now.year - YEAR_AGO..now.year + YEAR_NEXT).map { year ->
            Year(
                year = year,
                months = (1..12).map { month ->
                    createMonth(year, month)
                }
            )
        }
        val positionActualDay = years.indexOfFirst { it.year == now.year } * 12 + now.monthValue - 1

        return Pair(years, positionActualDay)
    }

    private fun createMonth(year: Int, month: Int): Month {
        val yearMonth = YearMonth.of(year, month)
        return Month(
            monthName = yearMonth.format(monthFormatter).capitalize(ILocale.current),
            days = (1..yearMonth.lengthOfMonth()).map { day ->
                Day(day)
            },
            offset = (yearMonth.atDay(1).dayOfWeek.value - 1) % 7
        )
    }
}