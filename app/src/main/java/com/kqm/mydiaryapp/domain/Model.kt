package com.kqm.mydiaryapp.domain

import com.kqm.mydiaryapp.framework.parseIdRelation
import java.time.LocalDate

data class Quote(
    val hour: String,
    val note: String,
    val quoteType: QuoteType,
    var isAlarm: Boolean = false
)

enum class QuoteType {
    FAMILIAR, PERSONAL, AMISTAD, TRABAJO
}

data class Day(
    val day: Int,
    val idRelation: String,
    var quotes: List<Quote> = emptyList()
) {
    val isCurrentDay: Boolean
        get() = isCurrentDay(idRelation)

    private fun isCurrentDay(idRelation: String): Boolean {
        val (day, month, year) = parseIdRelation(idRelation)
        val monthNumber = getMonthNumber(month)
        return LocalDate.now().isEqual(LocalDate.of(year, monthNumber, day))
    }

    private fun getMonthNumber(monthName: String): Int {
        return when (monthName) {
            "Enero" -> 1
            "Febrero" -> 2
            "Marzo" -> 3
            "Abril" -> 4
            "Mayo" -> 5
            "Junio" -> 6
            "Julio" -> 7
            "Agosto" -> 8
            "Septiembre" -> 9
            "Octubre" -> 10
            "Noviembre" -> 11
            "Diciembre" -> 12
            else -> 0
        }
    }
}

data class Month(
    val monthName: String,
    val days: List<Day>,
    var offset: Int = 0
)

data class Year(
    val year: Int,
    val months: List<Month>
)