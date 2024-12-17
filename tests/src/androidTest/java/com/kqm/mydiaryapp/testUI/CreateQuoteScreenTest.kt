package com.kqm.mydiaryapp.testUI

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.ui.screens.CreateQuote
import com.kqm.mydiaryapp.ui.screens.CreateQuoteState
import com.kqm.mydiaryapp.ui.screens.rememberCreateQuoteState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class CreateQuoteScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun whenNavigateForCreateQuote_shouldDisplayCreateQuoteScreen(): Unit = with(composeRule) {
        val dayId = "2-Enero-2024"
        val quote = createDays(true)[2].quotes[1]
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeRule.setContent {
            val state = rememberCreateQuoteState(dayId, quote.id)
            val timePickerState = TimePickerState(
                initialHour = state.timeState.hour,
                initialMinute = state.timeState.minute,
                is24Hour = true
            )

            CreateQuote(
                state = state,
                quote = quote,
                isUpdating = false,
                timePickerState = timePickerState,
                context = context,
                dayId = dayId,
                onBack = {},
                onUpdateClick = { _, _ -> },
                onAddClick = { _, _ -> }
            )
        }

        onNodeWithTag("CreateQuoteScreen").assertIsDisplayed()
    }

    @Test
    fun whenNavigateForUpdateQuote_shouldDisplayUpdateQuoteScreen(): Unit = with(composeRule) {
        var valueFirstArg = ""
        var valueSecondArg = Quote(id = 0, hour = "", note = "", quoteType = QuoteType.PERSONAL, isAlarm = false)
        val dayId = "2-Enero-2024"
        val quote = createDays(true)[2].quotes[1]
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeRule.setContent {
            val state = rememberCreateQuoteState(dayId, quote.id)
            val timePickerState = TimePickerState(
                initialHour = state.timeState.hour,
                initialMinute = state.timeState.minute,
                is24Hour = true
            )

            CreateQuote(
                state = state,
                quote = quote,
                isUpdating = true,
                timePickerState = timePickerState,
                context = context,
                dayId = dayId,
                onBack = {},
                onUpdateClick = { day, quote -> valueFirstArg = day; valueSecondArg = quote },
                onAddClick = { _, _ -> }
            )
        }

        onNodeWithTag("CreateQuoteScreen").assertIsDisplayed()
        onNodeWithTag("ButtonSaveQuote").performClick()

        assertEquals(valueFirstArg, dayId)
        assertEquals(valueSecondArg, quote)
    }

    @Test
    fun whenCallOnBack_callFunctionOnBack(): Unit = with(composeRule) {
        var called = false
        val dayId = "2-Enero-2024"
        val quote = createDays(true)[2].quotes[1]
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeRule.setContent {
            val state = rememberCreateQuoteState(dayId, quote.id)
            val timePickerState = TimePickerState(
                initialHour = state.timeState.hour,
                initialMinute = state.timeState.minute,
                is24Hour = true
            )

            CreateQuote(
                state = state,
                quote = quote,
                isUpdating = false,
                timePickerState = timePickerState,
                context = context,
                dayId = dayId,
                onBack = { called = true },
                onUpdateClick = { _, _ -> },
                onAddClick = { _, _ -> }
            )
        }

        onNodeWithTag("CreateQuoteScreen").assertIsDisplayed()
        onNodeWithContentDescription("Back").performClick()

        assertEquals(called, true)
    }

    @Test
    fun whenCallOnAddClick_callFunctionOnAddClick(): Unit = with(composeRule) {
        var valueFirstArg = ""
        var valueSecondArg = Quote(id = 0, hour = "05:00", note = "", quoteType = QuoteType.TRABAJO, isAlarm = false)
        val newQuote = createDays(true)[2].quotes[1]
        val dayId = "2-Enero-2024"
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeRule.setContent {
            val state = rememberCreateQuoteState(dayId, null)
            val timePickerState = TimePickerState(
                initialHour = state.timeState.hour,
                initialMinute = state.timeState.minute,
                is24Hour = true
            )

            CreateQuote(
                state = state,
                quote = valueSecondArg,
                isUpdating = false,
                timePickerState = timePickerState,
                context = context,
                dayId = dayId,
                onBack = {},
                onUpdateClick = { _, _ -> },
                onAddClick = { day, _ -> valueFirstArg = day; valueSecondArg = newQuote }
            )
        }

        onNodeWithTag("CreateQuoteScreen").assertIsDisplayed()
        onNodeWithTag("ButtonSaveQuote").performClick()

        assertEquals(valueFirstArg, dayId)
        assertEquals(valueSecondArg, Quote(id = 0, hour = "05:00", note = "", quoteType = QuoteType.TRABAJO, isAlarm = false))
    }
}