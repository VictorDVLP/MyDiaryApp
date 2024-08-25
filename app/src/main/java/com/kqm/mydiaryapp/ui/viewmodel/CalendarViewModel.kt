package com.kqm.mydiaryapp.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.usecases.GetDatesUseCase

class CalendarViewModel(
    getDatesUseCase: GetDatesUseCase
): ViewModel() {

    private val _getCalendar = mutableStateOf<List<Year>>(emptyList())
    val getCalendar: State<List<Year>> = _getCalendar

    private val _initialPosition = mutableStateOf(0)
    val initialPosition: State<Int> = _initialPosition

    init {
        val (years, position) = getDatesUseCase()
            _getCalendar.value = years
            _initialPosition.value = position
    }
}