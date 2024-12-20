package com.kqm.mydiaryapp.usecases

import androidx.paging.PagingData
import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.domain.Year
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDatesUseCase @Inject constructor(private val repository: CalendarRepository) {

    operator fun invoke(): Flow<PagingData<Year>> =
        repository.getCalendarWithQuotes()
}