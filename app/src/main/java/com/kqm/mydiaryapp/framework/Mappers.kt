package com.kqm.mydiaryapp.framework

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.domain.QuoteType.*
import com.kqm.mydiaryapp.framework.local.DayWithQuotes
import com.kqm.mydiaryapp.framework.local.LocalDay
import com.kqm.mydiaryapp.framework.local.LocalQuote

fun Quote.toLocalQuote(day: String): LocalQuote {
    return LocalQuote(
        hour = hour,
        quote = note,
        typeQuote = quoteType.toConvertName(),
        dayId = day
    )
}

fun String.toLocalDay(): LocalDay {
    return LocalDay(idRelation = this)
}

private fun QuoteType.toConvertName(): String {
    return when (this) {
        PERSONAL -> "Personal"
        FAMILY -> "Familiar"
        FRIEND -> "Amigos"
        WORK -> "Trabajo"
    }
}

fun DayWithQuotes.toDay(): Day {
    val day = parseIdRelation(date.idRelation).first
    return Day(
        day = day,
        idRelation = date.idRelation,
        quotes = quotes.map { it.toQuote() }
    )
}

fun generateIdRelation(day: Int, month: String, year: Int): String {
    return "$day-$month-$year"
}

fun parseIdRelation(idRelation: String): Triple<Int, String, Int> {
    val (day, month, year) = idRelation.split("-")
    return Triple(day.toInt(), month, year.toInt())
}


fun LocalQuote.toQuote(): Quote {
    val quoteType = when(typeQuote) {
        "Personal" -> PERSONAL
        "Familiar" -> FAMILY
        "Amigos" -> FRIEND
        "Trabajo" -> WORK
        else -> PERSONAL
    }
    return Quote(
        hour = hour,
        note = quote,
        quoteType = quoteType
    )
}