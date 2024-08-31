package com.kqm.mydiaryapp.data

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.framework.CalendarDataSourceImpl
import com.kqm.mydiaryapp.framework.QuoteDataSourceImpl

class CalendarRepository(
   private val calendarDataSource: CalendarDataSourceImpl,
   private val quotesDataSource: QuoteDataSourceImpl
) {
    fun getCalendar(): Pair<List<Year>, Int> = calendarDataSource.getRangeDates()

    fun getQuotes() = quotesDataSource.getQuotes()

    suspend fun insertQuote(day: Day) = quotesDataSource.insertQuote(day = day)

    suspend fun deleteQuote(quote: Quote, day: Day) = quotesDataSource.deleteQuote(quote = quote, day = day)
}