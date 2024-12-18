package com.kqm.mydiaryapp.testIntegration

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.kqm.mydiaryapp.fakes.buildViewModel
import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.helpers.createYear
import com.kqm.mydiaryapp.rules.CoroutinesTestRule
import com.kqm.mydiaryapp.ui.viewmodel.ResultCall
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CalendarTestIntegration {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `when initialize calendar view model should return a calendar`(): Unit = runTest {
        val expectedCalendar = createYear(false)

        val result = buildViewModel(calendar = expectedCalendar).calendarWithQuotes.asSnapshot()

        assertEquals(expectedCalendar, result)
    }

    @Test
    fun `when call get quotes for day should return a list of quotes`(): Unit = runTest {
        val expectedDay = createDays(true)[0]

        val viewModel = buildViewModel(createYear(false), createDays(true))

        viewModel.getQuotesOfDay("1-Enero-2024").test {
            assertEquals(ResultCall.Loading, awaitItem())
            assertEquals(ResultCall.Success(expectedDay), awaitItem())
        }
    }

    @Test
    fun `when call get quote by id should return a quote`(): Unit = runTest {
        val expectedQuote = createDays(true)[2].quotes[1]

        val viewModel = buildViewModel(createYear(false), createDays(true))

        viewModel.getQuoteById(2, "3-Enero-2024").test {
            assertEquals(ResultCall.Loading, awaitItem())
            assertEquals(ResultCall.Success(expectedQuote), awaitItem())
        }
    }

//    @Test
//    fun `when call add quote should add a quote`() = runTest {
//        val newQuote = Quote(3, "21:00", "Videollamada familiar", QuoteType.FAMILIAR, false)
//        val viewModel = buildViewModel(createYear(false), createDays(true))
//        val expectedDay = createDays(true)[2].copy(quotes = createDays(true)[2].quotes + newQuote)
//
//        viewModel.addQuote("3-Enero-2024", newQuote)
//        runCurrent()
//        viewModel.getQuotesOfDay("3-Enero-2024").test {
//            assertEquals(ResultCall.Loading, awaitItem())
//            advanceUntilIdle()
//            assertEquals(ResultCall.Success(expectedDay), awaitItem())
//        }
//    }
//
//    @Test
//    fun `when call update quote should update a quote`(): Unit = runTest {
//        val updateQuote = createDays(true)[0].quotes[0].copy(hour = "13:20")
//        val viewModel = buildViewModel(createYear(false), createDays(true))
//        val expectedDay = Day(
//            1,
//            "1-Enero-2024",
//            listOf(
//                Quote(
//                    id = 1,
//                    hour = "13:20",
//                    note = "Dentista",
//                    quoteType = QuoteType.PERSONAL,
//                    isAlarm = true
//                ),
//                Quote(
//                    id = 2,
//                    hour = "18:00",
//                    note = "Padel",
//                    quoteType = QuoteType.AMISTAD,
//                    isAlarm = false
//                )
//            )
//        )
//
//        viewModel.updateQuote("1-Enero-2024", updateQuote)
//
//        viewModel.getQuotesOfDay("1-Enero-2024").test {
//            assertEquals(ResultCall.Loading, awaitItem())
//            advanceUntilIdle()
//            assertEquals(ResultCall.Success(expectedDay), awaitItem())
//        }
//    }
//
//    @Test
//    fun `when call delete quote should delete a quote`(): Unit = runTest {
//        val viewModel = buildViewModel(createYear(false), createDays(true))
//        val expectedDay = createDays(true)[0].copy(
//            quotes = createDays(true)[0].quotes - createDays(true)[0].quotes[0]
//        )
//
//        viewModel.deleteQuote(createDays(true)[0].quotes[0], "1-Enero-2024")
//
//        viewModel.getQuotesOfDay("1-Enero-2024").test {
//            assertEquals(ResultCall.Loading, awaitItem())
//            advanceUntilIdle()
//            assertEquals(ResultCall.Success(expectedDay), awaitItem())
//        }
//    }
}