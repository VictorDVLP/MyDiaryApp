package com.kqm.mydiaryapp.usecases

import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.domain.Year
import javax.inject.Inject

class GetDatesUseCase @Inject constructor(private val repository: CalendarRepository) {

    operator fun invoke(): Pair<List<Year>, Int> = repository.calendar
}