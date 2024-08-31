package com.kqm.mydiaryapp.data

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteDataSource {
    fun getQuotes(): Flow<List<Day>>

    suspend fun insertQuote(day: Day)

    suspend fun deleteQuote(quote: Quote, day: Day)
}