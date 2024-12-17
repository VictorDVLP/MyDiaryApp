package com.kqm.mydiaryapp.framework

import com.kqm.mydiaryapp.data.QuoteDataSource
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.framework.local.DayDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuoteDataSourceImpl @Inject constructor(private val dao: DayDao) : QuoteDataSource {

    override fun getQuotes(): Flow<List<Day>> =
        dao.getAllQuotes().map { dayWithQuotes -> dayWithQuotes.map { it.toDay() } }

    override fun getQuotesOfDay(dayId: String): Flow<Day> {
        return dao.getQuotesOfDay(dayId).map {
            it?.toDay()
                ?: Day(
                    day = parseIdRelation(dayId).first,
                    idRelation = dayId
                )
        }
    }

    override fun getQuoteById(quoteId: Int, dayId: String): Flow<Quote> {
        return dao.getQuoteById(quoteId, dayId).map { it.toQuote() }
    }

    override suspend fun insertQuote(day: String, quote: Quote) {
        val lastId = dao.getLastQuotesIdForDay(day).first() + 1
        val quoteId = quote.copy(id = lastId)
        dao.inserts( date = day.toLocalDay(), quotes = quoteId.toLocalQuote(day = day))
    }

    override suspend fun updateQuote(day: String, quote: Quote) {
        dao.updateQuote(quote = quote.toLocalQuote(day = day))
    }

    override suspend fun deleteQuote(quote: Quote, day: String) {
        dao.deleteQuote(quote = quote.toLocalQuote(day = day))
    }
}