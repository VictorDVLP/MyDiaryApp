package com.kqm.mydiaryapp.helpers

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Month
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.domain.Year

fun createDays(withQuotes: Boolean): List<Day> {
    return (1..3).map { day ->
        val quotes = if (withQuotes) {
            when (day) {
                1 -> listOf(
                    Quote(id = 1, hour = "10:30", note = "Dentista", quoteType = QuoteType.PERSONAL, isAlarm = true),
                    Quote(id = 2, hour = "18:00", note = "Padel", quoteType = QuoteType.AMISTAD, isAlarm = false)
                )
                2 -> listOf(
                    Quote(id = 1, hour = "13:30", note = "Entrevista de trabajo", quoteType = QuoteType.TRABAJO, isAlarm = true)
                )
                3 -> listOf(
                    Quote(id = 1, hour = "12:30", note = "Gimnasio", quoteType = QuoteType.PERSONAL, isAlarm = true),
                    Quote(id = 2, hour = "18:00", note = "Partido de futbol", quoteType = QuoteType.AMISTAD, isAlarm = false)
                )
                else -> emptyList()
            }
        } else {
            emptyList()
        }
        Day(day = day, idRelation = "$day-Enero-2024", quotes = quotes)
    }
}

fun createYear(withQuotes: Boolean): List<Year> {
    return listOf(
        Year(year = 2024, months = listOf(Month(monthName = "Noviembre", days = createDays(withQuotes), offset = 4))),
        Year(year = 2025, months = listOf(Month(monthName = "Diciembre", days = createDays(withQuotes), offset = 2))),
        Year(year = 2026, months = listOf(Month(monthName = "Enero", days = createDays(withQuotes), offset = 3))),
    )
}
