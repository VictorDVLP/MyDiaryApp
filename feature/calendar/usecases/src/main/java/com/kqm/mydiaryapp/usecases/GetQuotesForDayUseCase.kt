package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import javax.inject.Inject

class GetQuotesForDayUseCase @Inject constructor(private val repository: CalendarRepository) {

    operator fun invoke(dayId: String) = repository.quotesForDay(dayId = dayId)
}
