package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.domain.Quote
import javax.inject.Inject

class UpdateQuoteUseCases @Inject constructor( private val repository: CalendarRepository) {

    suspend operator fun invoke(day: String, quote: Quote) = repository.updateQuote(day = day, quote = quote)
}