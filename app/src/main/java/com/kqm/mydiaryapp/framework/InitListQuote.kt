package com.kqm.mydiaryapp.framework

import com.kqm.mydiaryapp.domain.Quote
import java.util.Locale

fun generateListQuotes(): List<Quote> {
    val quotes = mutableListOf<Quote>()
    for (hora in 5..23) {
        for (minute in 0..45 step 15) {
            val hourFormated = String.format(Locale.getDefault(), "%02d:%02d", hora, minute)
            val quote = Quote(hourFormated, "", null)
            quotes.add(quote)
        }
    }
    return quotes
}