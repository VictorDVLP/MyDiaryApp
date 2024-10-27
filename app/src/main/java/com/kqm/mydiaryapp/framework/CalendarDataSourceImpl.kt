package com.kqm.mydiaryapp.framework

import androidx.compose.ui.text.capitalize
import com.kqm.mydiaryapp.data.CalendarDataSource
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Month
import com.kqm.mydiaryapp.domain.Year
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import androidx.compose.ui.text.intl.Locale as ILocale

const val YEAR_AGO = 0
const val YEAR_NEXT = 5

class CalendarDataSourceImpl @Inject constructor() : CalendarDataSource {

    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale("es", "ES"))

    override fun getRangeDates(): Pair<List<Year>, Int> {
        val now = LocalDate.now()
        val years = (now.year - YEAR_AGO..now.year + YEAR_NEXT).map { year ->
            Year(
                year = year,
                months = (1..12).map { month ->
                    createMonth(year, month, now)
                }
            )
        }
        val initialPosition = years.indexOfFirst { it.year == now.year }

        return Pair(years, initialPosition)
    }

    private fun createMonth(year: Int, month: Int, now: LocalDate): Month {
        val yearMonth = YearMonth.of(year, month)
        return Month(
            monthName = yearMonth.format(monthFormatter).capitalize(ILocale.current),
            days = (1..yearMonth.lengthOfMonth()).map { day ->
                Day(
                    day = day,
                    idRelation = generateIdRelation(day, month.toConvertName(), year)
                )
            },
            offset = (yearMonth.atDay(1).dayOfWeek.value - 1) % 7
        )
    }

    private fun Int.toConvertName(): String {
        return when (this) {
            1 -> "Enero"
            2 -> "Febrero"
            3 -> "Marzo"
            4 -> "Abril"
            5 -> "Mayo"
            6 -> "Junio"
            7 -> "Julio"
            8 -> "Agosto"
            9 -> "Septiembre"
            10 -> "Octubre"
            11 -> "Noviembre"
            12 -> "Diciembre"
            else -> ""
        }
    }
}
