package com.kqm.mydiaryapp.fakes

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import com.kqm.mydiaryapp.usecases.AddQuoteUseCase
import com.kqm.mydiaryapp.usecases.DeleteQuoteUseCase
import com.kqm.mydiaryapp.usecases.GetDatesUseCase
import com.kqm.mydiaryapp.usecases.GetQuoteByIdUseCases
import com.kqm.mydiaryapp.usecases.GetQuotesForDayUseCase
import com.kqm.mydiaryapp.usecases.UpdateQuoteUseCases

fun buildViewModel(calendar: List<Year> = emptyList(), quotes: List<Day> = emptyList()): CalendarViewModel {
    return CalendarViewModel(
        getDatesUseCase = GetDatesUseCase(buildCalendarRepository(calendar, quotes)),
        getQuotesForDayUseCase = GetQuotesForDayUseCase(buildCalendarRepository(calendar, quotes)),
        getQuoteByIdUseCases = GetQuoteByIdUseCases(buildCalendarRepository(calendar, quotes)),
        addQuoteUseCase = AddQuoteUseCase(buildCalendarRepository(calendar, quotes)),
        deleteQuoteUseCase = DeleteQuoteUseCase(buildCalendarRepository(calendar, quotes)),
        updateQuoteUseCases = UpdateQuoteUseCases(buildCalendarRepository(calendar, quotes))
    )
}