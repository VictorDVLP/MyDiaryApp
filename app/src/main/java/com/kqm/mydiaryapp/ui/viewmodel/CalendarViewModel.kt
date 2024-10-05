package com.kqm.mydiaryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.usecases.AddQuoteUseCase
import com.kqm.mydiaryapp.usecases.DeleteQuoteUseCase
import com.kqm.mydiaryapp.usecases.GetDateByIdUseCase
import com.kqm.mydiaryapp.usecases.GetDatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    getDatesUseCase: GetDatesUseCase,
    private val getDateByIdUseCase: GetDateByIdUseCase,
    private val addQuoteUseCase: AddQuoteUseCase,
    private val deleteQuoteUseCase: DeleteQuoteUseCase
) : ViewModel() {

    val calendarWithQuotes: StateFlow<ResultCall<Pair<List<Year>, Int>>> =
        getDatesUseCase().stateAsResultIn(viewModelScope)

    fun getQuotesOfDay(dayId: String): StateFlow<ResultCall<Day>> =
        getDateByIdUseCase(dayId).stateAsResultIn(viewModelScope)

    fun addQuote(dayId: String, quote: Quote) {
        viewModelScope.launch {
            addQuoteUseCase(day = dayId, quote = quote)
        }
    }

    fun deleteQuote(quote: Quote, dayId: String) {
        viewModelScope.launch {
            deleteQuoteUseCase(quote, day = dayId)
        }
    }
}
