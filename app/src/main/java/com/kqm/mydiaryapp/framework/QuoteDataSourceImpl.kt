package com.kqm.mydiaryapp.framework

import com.kqm.mydiaryapp.data.QuoteDataSource
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.framework.local.DayDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuoteDataSourceImpl(private val dao: DayDao) : QuoteDataSource {

    override fun getQuotes(): Flow<List<Day>> = dao.getQuotes().map { dayWithQuotes -> dayWithQuotes.map { it.toDay() } }

    override suspend fun insertQuote(day: Day) {
        dao.inserts(quotes = day.toDayWithQuotes())
    }

    override suspend fun deleteQuote(quote: Quote, day: Day) {
        dao.deleteDay(quote = quote.toLocalQuote(day = day.day))
    }
}