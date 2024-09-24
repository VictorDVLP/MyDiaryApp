package com.kqm.mydiaryapp.framework

import android.util.Log
import com.kqm.mydiaryapp.data.QuoteDataSource
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.framework.local.DayDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class QuoteDataSourceImpl @Inject constructor(private val dao: DayDao) : QuoteDataSource {

    override fun getQuotes(): Flow<List<Day>> =
        dao.getAllQuotes().map { dayWithQuotes -> dayWithQuotes.map { it.toDay() } }
            .onEach { Log.i("TAG", "getQuotes in database: $it") }

    override fun getQuotesOfDay(dayId: String): Flow<Day> {
        return dao.getQuotesOfDay(dayId).map {
            it?.toDay()
                ?: Day(
                    day = parseIdRelation(dayId).first,
                    idRelation = dayId
                )
        }.onEach { Log.i("TAG", "getQuotesOfDay: ${it.quotes}") }
    }

    override suspend fun insertQuote(day: String, quote: Quote) {
        dao.inserts( date = day.toLocalDay(), quotes = quote.toLocalQuote(day = day))
    }

    override suspend fun deleteQuote(quote: Quote, day: String) {
        dao.deleteQuote(quote = quote.toLocalQuote(day = day))
    }
}