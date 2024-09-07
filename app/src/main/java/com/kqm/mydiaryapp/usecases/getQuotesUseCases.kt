package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import javax.inject.Inject

class GetQuotesUseCases @Inject constructor(private val repository: CalendarRepository) {

    operator fun invoke() = repository.quotes
}