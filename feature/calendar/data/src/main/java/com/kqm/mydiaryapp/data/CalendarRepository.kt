package com.kqm.mydiaryapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kqm.mydiaryapp.data.pagination.CalendarPagingSource
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.Year
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarRepository @Inject constructor(
    private val calendarPagingSource: CalendarPagingSource,
    private val quotesDataSource: QuoteDataSource
) {

    fun getCalendarWithQuotes(): Flow<PagingData<Year>> {
        return Pager(
            config = PagingConfig(pageSize = 3, initialLoadSize = 2, prefetchDistance = 1),
            pagingSourceFactory = { calendarPagingSource }
        ).flow.flatMapLatest { pagingData ->
            quotesDataSource.getQuotes().map { quotes ->
                pagingData.map { year ->
                    year.addQuotesToDays(quotes)
                }
            }
        }
    }

     private fun Year.addQuotesToDays(quotes: List<Day>): Year {
        val quotesMap = quotes.associateBy { it.idRelation }
        return this.copy(months = this.months.map { month ->
            month.copy(days = month.days.map { day ->
                val dayQuotesId = day.idRelation
                val dayQuotes = quotesMap[dayQuotesId]?.quotes ?: emptyList()
                day.copy(quotes = dayQuotes)
            })
        })
    }

    fun quotesForDay(dayId: String): Flow<Day> {
        return quotesDataSource.getQuotesOfDay(dayId)
            .map { dayWithQuotes ->
                dayWithQuotes.let {
                    it.copy(quotes = it.quotes.sortedBy { quote ->
                        quote.hour
                    })
                }
            }
    }

    fun getQuoteById(id: Int, dayId: String): Flow<Quote> =
        quotesDataSource.getQuoteById(id, dayId)

    suspend fun insertQuote(day: String, quote: Quote) =
        quotesDataSource.insertQuote(day = day, quote = quote)

    suspend fun updateQuote(day: String, quote: Quote) =
        quotesDataSource.updateQuote(day = day, quote = quote)

    suspend fun deleteQuote(quote: Quote, day: String) =
        quotesDataSource.deleteQuote(quote = quote, day = day)
}
