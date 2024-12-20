package com.kqm.mydiaryapp.data

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteDataSource {
    fun getQuotes(): Flow<List<Day>>

    fun getQuotesOfDay(dayId: String): Flow<Day>

    fun getQuoteById(quoteId: Int, dayId: String): Flow<Quote>

    suspend fun updateQuote(day: String, quote: Quote)

    suspend fun insertQuote(day: String, quote: Quote)

    suspend fun deleteQuote(quote: Quote, day: String)
}