package com.kqm.mydiaryapp.data

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.framework.CalendarDataSourceImpl
import com.kqm.mydiaryapp.framework.QuoteDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CalendarRepository @Inject constructor(
   calendarDataSource: CalendarDataSourceImpl,
   private val quotesDataSource: QuoteDataSourceImpl
) {
    val calendar: Pair<List<Year>, Int> = calendarDataSource.getRangeDates()

    val quotes: Flow<List<Day>> = quotesDataSource.getQuotes()

    suspend fun insertQuote(day: Day) = quotesDataSource.insertQuote(day = day)

    suspend fun deleteQuote(quote: Quote, day: Day) = quotesDataSource.deleteQuote(quote = quote, day = day)
}