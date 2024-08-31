package com.kqm.mydiaryapp.framework

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.framework.local.DayWithQuotes
import com.kqm.mydiaryapp.framework.local.LocalDay
import com.kqm.mydiaryapp.framework.local.LocalQuote

fun Day.toDayWithQuotes(): DayWithQuotes {
    return DayWithQuotes(day = this.day.toLocalDay(), quotes = quotes.map { it.toLocalQuote(day = day) })
}

fun Quote.toLocalQuote(day: Int): LocalQuote {
    return LocalQuote(
        hour = hour,
        quote = note,
        typeQuote = quoteType.toConvertName(),
        dayId = day
    )
}

private fun Int.toLocalDay(): LocalDay {
    return LocalDay(day = this)
}

private fun QuoteType.toConvertName(): String {
    return when (this) {
        QuoteType.PERSONAL -> "Personal"
        QuoteType.FAMILY -> "Familiar"
        QuoteType.FRIEND -> "Amigos"
        QuoteType.WORK -> "Trabajo"
    }
}

fun DayWithQuotes.toDay(): Day {
    return Day(
        day = day.day,
        quotes = quotes.map { it.toQuote() }
    )
}

fun LocalQuote.toQuote(): Quote {
    return Quote(
        hour = hour,
        note = quote,
        quoteType = QuoteType.valueOf(typeQuote.uppercase())
    )
}