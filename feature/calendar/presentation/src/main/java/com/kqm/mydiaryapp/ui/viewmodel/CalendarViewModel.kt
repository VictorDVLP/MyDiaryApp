package com.kqm.mydiaryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.usecases.AddQuoteUseCase
import com.kqm.mydiaryapp.usecases.DeleteQuoteUseCase
import com.kqm.mydiaryapp.usecases.GetDatesUseCase
import com.kqm.mydiaryapp.usecases.GetQuoteByIdUseCases
import com.kqm.mydiaryapp.usecases.GetQuotesForDayUseCase
import com.kqm.mydiaryapp.usecases.UpdateQuoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    getDatesUseCase: GetDatesUseCase,
    private val getQuotesForDayUseCase: GetQuotesForDayUseCase,
    private val addQuoteUseCase: AddQuoteUseCase,
    private val updateQuoteUseCases: UpdateQuoteUseCases,
    private val deleteQuoteUseCase: DeleteQuoteUseCase,
    private val getQuoteByIdUseCases: GetQuoteByIdUseCases
) : ViewModel() {

    val calendarWithQuotes: Flow<PagingData<Year>> = getDatesUseCase()

    fun getQuotesOfDay(dayId: String): StateFlow<ResultCall<Day>> =
        getQuotesForDayUseCase(dayId).stateAsResultIn(viewModelScope)

    fun getQuoteById(id: Int, dayId: String): StateFlow<ResultCall<Quote>> =
        getQuoteByIdUseCases(id, dayId).stateAsResultIn(viewModelScope)

    fun addQuote(dayId: String, quote: Quote) {
        viewModelScope.launch {
            addQuoteUseCase(day = dayId, quote = quote)
        }
    }

    fun updateQuote(dayId: String, quote: Quote) {
        viewModelScope.launch {
            updateQuoteUseCases(day = dayId, quote = quote)
        }
    }

    fun deleteQuote(quote: Quote, dayId: String) {
        viewModelScope.launch {
            deleteQuoteUseCase(quote, day = dayId)
        }
    }
}
