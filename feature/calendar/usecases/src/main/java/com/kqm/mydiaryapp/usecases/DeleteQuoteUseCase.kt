package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.domain.Quote
import javax.inject.Inject

class DeleteQuoteUseCase @Inject constructor(private val repository: CalendarRepository) {

    suspend operator fun invoke(quote: Quote, day: String) = repository.deleteQuote(quote = quote, day = day)
}