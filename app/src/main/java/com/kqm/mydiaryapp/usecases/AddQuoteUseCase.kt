package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.domain.Day

class AddQuoteUseCase(private val repository: CalendarRepository) {

    suspend operator fun invoke(day: Day) = repository.insertQuote(day = day)
}