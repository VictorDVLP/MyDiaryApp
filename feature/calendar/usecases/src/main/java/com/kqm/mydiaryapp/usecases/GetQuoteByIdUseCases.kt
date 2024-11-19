package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import javax.inject.Inject

class GetQuoteByIdUseCases @Inject constructor( private val repository: CalendarRepository) {

    operator fun invoke(quoteId: Int, dayId: String) = repository.getQuoteById(id = quoteId, dayId = dayId)
}