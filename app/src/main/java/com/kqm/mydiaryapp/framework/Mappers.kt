package com.kqm.mydiaryapp.framework

import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.framework.local.LocalQuote

fun Quote.toLocalQuote(day: Int): LocalQuote {
    return LocalQuote(
        hour = hour,
        quote = note,
        typeQuote = quoteType.toConvertName(),
        dayId = day
    )
}

private fun QuoteType.toConvertName(): String {
    return when (this) {
        QuoteType.PERSONAL -> "Personal"
        QuoteType.FAMILY -> "Familiar"
        QuoteType.FRIEND -> "Amigos"
        QuoteType.WORK -> "Trabajo"
    }
}

fun LocalQuote.toQuote(): Quote {
    return Quote(
        hour = hour,
        note = quote,
        quoteType = QuoteType.valueOf(typeQuote.uppercase())
    )
}