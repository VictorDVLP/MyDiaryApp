package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.domain.Year

class GetDatesUseCase(private val repository: CalendarRepository) {

    operator fun invoke(): Pair<List<Year>, Int> = repository.getCalendar()
}