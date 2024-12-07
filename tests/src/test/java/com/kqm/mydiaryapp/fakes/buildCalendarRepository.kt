package com.kqm.mydiaryapp.fakes

import com.kqm.mydiaryapp.data.CalendarDataSource
import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.data.QuoteDataSource
import com.kqm.mydiaryapp.data.pagination.CalendarPagingSource
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.Year
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

fun buildCalendarRepository(calendar: List<Year>, quotes: List<Day>): CalendarRepository {
    return CalendarRepository(
        calendarPagingSource = CalendarPagingSource(FakeCalendarDataSource(calendar)),
        quotesDataSource = FakeQuotesDataSource().apply { inMemoryQuotes.value = quotes }
    )
}

class FakeCalendarDataSource(private val inMemoryCalendar: List<Year>) : CalendarDataSource {

    override fun getRangeDates(): Pair<List<Year>, Int> = Pair(inMemoryCalendar, 0)
}

class FakeQuotesDataSource : QuoteDataSource {

    var inMemoryQuotes = MutableStateFlow<List<Day>>(emptyList())

    override fun getQuotes(): Flow<List<Day>> = inMemoryQuotes

    override fun getQuotesOfDay(dayId: String): Flow<Day> {
        inMemoryQuotes.map { days -> days.first { it.idRelation == dayId } }.also { return it }
    }

    override fun getQuoteById(quoteId: Int, dayId: String): Flow<Quote> {
        inMemoryQuotes.map { days -> days.first { it.idRelation == dayId }.quotes.first { it.id == quoteId } }
            .also { return it }
    }

    override suspend fun updateQuote(day: String, quote: Quote) {
        inMemoryQuotes.map { days ->
            days.first { it.idRelation == day }.quotes.first { it.id == quote.id }.copy(
                id = quote.id,
                hour = quote.hour,
                note = quote.note,
                quoteType = quote.quoteType,
                isAlarm = quote.isAlarm
            )
        }
    }

    override suspend fun insertQuote(day: String, quote: Quote) {
        val updatedQuotes = inMemoryQuotes.value.map { dayItem ->
            if (dayItem.idRelation == day) {
                dayItem.copy(quotes = dayItem.quotes + quote)
            } else {
                dayItem
            }
        }
        inMemoryQuotes.value = updatedQuotes
    }

    override suspend fun deleteQuote(quote: Quote, day: String) {
        inMemoryQuotes.map { days -> days.first { it.idRelation == day }.quotes.minus(quote) }
    }

}
