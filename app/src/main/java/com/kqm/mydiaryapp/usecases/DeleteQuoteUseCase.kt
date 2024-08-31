package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote

class DeleteQuoteUseCase(private val repository: CalendarRepository) {

    suspend operator fun invoke(quote: Quote, day: Day) = repository.deleteQuote(quote = quote, day = day)
}