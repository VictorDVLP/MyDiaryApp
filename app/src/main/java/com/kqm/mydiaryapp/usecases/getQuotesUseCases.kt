package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository

class GetQuotesUseCases(private val repository: CalendarRepository) {

    operator fun invoke() = repository.getQuotes()
}