package com.kqm.mydiaryapp.testUI

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.ui.screens.DayScreen
import com.kqm.mydiaryapp.ui.viewmodel.ResultCall
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DayDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenNavigateToDayDetail_showDayDetail(): Unit = with(composeTestRule) {
        val dataResult = ResultCall.Success(createDays(true)[0])
        setContent {
            DayScreen(
                resultCall = dataResult,
                dayId = "1-Enero-2024",
                onNavigateQuote = { _, _ -> },
                onBack = {},
                onDelete = { _, _ -> }
            )
        }

        onNodeWithTag("DayDetailScreen").assertIsDisplayed()
    }

    @Test
    fun whenNavigateToDayDetail_andShowLoadingBeforeData(): Unit = with(composeTestRule) {
        val dataResult = ResultCall.Loading
        setContent {
            DayScreen(
                resultCall = dataResult,
                dayId = "1-Enero-2024",
                onNavigateQuote = { _, _ -> },
                onBack = {},
                onDelete = { _, _ -> }
            )
        }

        onNodeWithTag("LoadingScreen").assertIsDisplayed()
    }

    @Test
    fun whenNavigateToDayDetail_andThereIsAnErrorLoadingData_showError(): Unit = with(composeTestRule) {
        val dataResult = ResultCall.Error(Exception("Error"))
        setContent {
            DayScreen(
                resultCall = dataResult,
                dayId = "1-Enero-2024",
                onNavigateQuote = { _, _ -> },
                onBack = {},
                onDelete = { _, _ -> }
            )
        }

        onNodeWithTag("ErrorScreen").assertIsDisplayed()
    }

    @Test
    fun whenCallOnNavigateQuote_callFunctionOnNavigateQuoteForUpdateQuote(): Unit = with(composeTestRule) {
        var dayId = ""
        var quoteId: Int? = null
        setContent {
            DayScreen(
                resultCall = ResultCall.Success(createDays(true)[1]),
                dayId = "2-Enero-2024",
                onNavigateQuote = { day, quote ->  dayId = day; quoteId = quote },
                onBack = {},
                onDelete = { _, _ -> }
            )
        }

        onNodeWithTag("UpdateQuote").performClick()

        assertEquals(dayId, "2-Enero-2024")
        assertEquals(quoteId, 1)
    }

    @Test
    fun whenCallOnDeleteQuote_callFunctionOnDeleteQuote(): Unit = with(composeTestRule) {
        var dayId = ""
        var quoteId: Int? = null
        setContent {
            DayScreen(
                resultCall = ResultCall.Success(createDays(true)[1]),
                dayId = "2-Enero-2024",
                onNavigateQuote = { _, _ -> },
                onBack = {},
                onDelete = { quote, day -> dayId = day; quoteId = quote.id }

            )
        }

        onNodeWithTag("DeleteQuote").performClick()

        assertEquals(dayId, "2-Enero-2024")
        assertEquals(quoteId, 1)
    }

    @Test
    fun whenCallOnBack_callFunctionOnBack(): Unit = with(composeTestRule) {
        var called = false
        setContent {
            DayScreen(
                resultCall = ResultCall.Success(createDays(true)[1]),
                dayId = "2-Enero-2024",
                onNavigateQuote = { _, _ -> },
                onBack = { called = true },
                onDelete = { _, _ -> }
            )
        }

        onNodeWithTag("BackButton").performClick()

        assertEquals(called, true)
    }
}