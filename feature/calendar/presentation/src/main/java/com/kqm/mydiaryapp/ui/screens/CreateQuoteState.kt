@file:OptIn(ExperimentalMaterial3Api::class)

package com.kqm.mydiaryapp.ui.screens

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import com.kqm.mydiaryapp.ui.viewmodel.ResultCall
import java.time.LocalTime
import com.kqm.mydiaryapp.notification.startNotification as notificationStart

interface CreateQuoteStateImpl {
    val idState: Int
    val timeState: LocalTime
    var textState: String
    var selectedQuoteType: QuoteType
    var alarm: Boolean
    fun updateQuote(quote: Quote)
    fun startNotification(context: Context, dayId: String, timePickerState: TimePickerState, noteText: String)
}

@ExperimentalMaterial3Api
class CreateQuoteState: CreateQuoteStateImpl {
    override var idState by mutableIntStateOf(0)
        private set
    override var timeState: LocalTime by mutableStateOf(LocalTime.of(5, 0))
        private set
    override var textState by mutableStateOf("")
    override var selectedQuoteType by mutableStateOf(QuoteType.TRABAJO)
    override var alarm by mutableStateOf(false)

    override fun updateQuote(quote: Quote) {
        idState = quote.id
        val (h, m) = quote.hour.split(":").map { it.toInt() }
        timeState = LocalTime.of(h, m)
        textState = quote.note
        selectedQuoteType = quote.quoteType
        alarm = quote.isAlarm
    }

    override fun startNotification(context: Context, dayId: String, timePickerState: TimePickerState, noteText: String) {
        notificationStart(context, dayId, timePickerState, noteText)
    }
}

@ExperimentalMaterial3Api
@Composable
fun rememberCreateQuoteState(
    dayId: String,
    quoteId: Int?
): CreateQuoteState {

    val state = remember(dayId, quoteId) {
        mutableStateOf( CreateQuoteState() )
    }

    return state.value
}

@Composable
fun UpdateQuoteState(
    viewModel: CalendarViewModel,
    dayId: String,
    quoteId: Int?,
    timePickerState: TimePickerState,
    state: CreateQuoteState
) {
    LaunchedEffect(quoteId) {
        if (quoteId != null) {
            viewModel.getQuoteById(quoteId, dayId).collect { result ->
                if (result is ResultCall.Success) {
                    state.updateQuote(result.value)
                    val (h, m) = result.value.hour.split(":").map { it.toInt() }
                    timePickerState.hour = h
                    timePickerState.minute = m
                }
            }
        }
    }
}