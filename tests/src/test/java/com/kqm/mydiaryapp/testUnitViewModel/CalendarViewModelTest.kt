package com.kqm.mydiaryapp.testUnitViewModel

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.helpers.createYear
import com.kqm.mydiaryapp.rules.CoroutinesTestRule
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import com.kqm.mydiaryapp.ui.viewmodel.ResultCall
import com.kqm.mydiaryapp.usecases.AddQuoteUseCase
import com.kqm.mydiaryapp.usecases.DeleteQuoteUseCase
import com.kqm.mydiaryapp.usecases.GetDatesUseCase
import com.kqm.mydiaryapp.usecases.GetQuoteByIdUseCases
import com.kqm.mydiaryapp.usecases.GetQuotesForDayUseCase
import com.kqm.mydiaryapp.usecases.UpdateQuoteUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CalendarViewModelTest {

    @get:Rule(order = 0)
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var getDatesUseCase: GetDatesUseCase

    @Mock
    private lateinit var getQuotesForDayUseCase: GetQuotesForDayUseCase

    @Mock
    private lateinit var getQuoteByIdUseCases: GetQuoteByIdUseCases

    @Mock
    private lateinit var addQuoteUseCase: AddQuoteUseCase

    @Mock
    private lateinit var updateQuoteUseCases: UpdateQuoteUseCases

    @Mock
    private lateinit var deleteQuoteUseCase: DeleteQuoteUseCase


    private lateinit var calendarViewModel: CalendarViewModel


    @Test
    fun `when state is initialized then getDatesUseCase is called and returns list of dates`(): Unit = runTest {

            val expectedData = createYear(false)
            whenever(getDatesUseCase.invoke()).thenReturn(flowOf(PagingData.from(expectedData)))
            calendarViewModel = CalendarViewModel(
                getDatesUseCase,
                getQuotesForDayUseCase,
                addQuoteUseCase,
                updateQuoteUseCases,
                deleteQuoteUseCase,
                getQuoteByIdUseCases
            )

            val actualData = calendarViewModel.calendarWithQuotes.asSnapshot()

            assertEquals(expectedData, actualData)
            verify(getDatesUseCase, times(1)).invoke()
        }

    @Test
    fun `when call getQuotesForDay then returns the corresponding day with the quotes`(): Unit = runTest {

        val dayId = "1-Enero-2024"
        val expectedDay = createDays(true)[0]

        whenever(getQuotesForDayUseCase.invoke(dayId)).thenReturn(flowOf(expectedDay))
        calendarViewModel = CalendarViewModel(
            getDatesUseCase,
            getQuotesForDayUseCase,
            addQuoteUseCase,
            updateQuoteUseCases,
            deleteQuoteUseCase,
            getQuoteByIdUseCases
        )

        calendarViewModel.getQuotesOfDay(dayId).test {
            assertEquals(ResultCall.Loading, awaitItem())
            assertEquals(ResultCall.Success(expectedDay), awaitItem())
        }
    }

    @Test
    fun `when call getQuoteById then returns the corresponding quote`(): Unit = runTest {
        val testQuoteId = 2
        val testDayId = "1-Enero-2024"
        val expectedDay = createDays(true)[0].quotes[1]

        whenever(getQuoteByIdUseCases(testQuoteId, testDayId)).thenReturn(flowOf(expectedDay))

        calendarViewModel = CalendarViewModel(
            getDatesUseCase,
            getQuotesForDayUseCase,
            addQuoteUseCase,
            updateQuoteUseCases,
            deleteQuoteUseCase,
            getQuoteByIdUseCases
        )

        calendarViewModel.getQuoteById(testQuoteId, testDayId).test {
            assertEquals(ResultCall.Loading, awaitItem())
            assertEquals(ResultCall.Success(expectedDay), awaitItem())
        }

        val quoteIdCaptor = argumentCaptor<Int>()
        val dayIdCaptor = argumentCaptor<String>()

        verify(getQuoteByIdUseCases, times(1)).invoke(quoteIdCaptor.capture(), dayIdCaptor.capture())
    }

    @Test
    fun `when call addQuote then addQuoteUseCase is called`(): Unit = runTest {
        val testQuote = createDays(true)[0].quotes[0]
        val expectedDayId = "1-Enero-2024"

        whenever(addQuoteUseCase.invoke(expectedDayId, testQuote)).thenReturn(Unit)

        calendarViewModel = CalendarViewModel(
            getDatesUseCase,
            getQuotesForDayUseCase,
            addQuoteUseCase,
            updateQuoteUseCases,
            deleteQuoteUseCase,
            getQuoteByIdUseCases
        )

        calendarViewModel.addQuote(expectedDayId, testQuote)

        advanceUntilIdle()

        val dayIdCaptor = argumentCaptor<String>()
        val quoteCaptor = argumentCaptor<Quote>()

        verify(addQuoteUseCase, times(1)).invoke(dayIdCaptor.capture(), quoteCaptor.capture())

        assertEquals(expectedDayId, dayIdCaptor.firstValue)
        assertEquals(testQuote, quoteCaptor.firstValue)

    }

    @Test
    fun `when call updateQuote then updateQuoteUseCase is called`(): Unit = runTest {
        val testQuote = createDays(true)[0].quotes[0].copy(hour = "13:45")
        val expectedDayId = "1-Enero-2024"

        whenever(updateQuoteUseCases.invoke(expectedDayId, testQuote)).thenReturn(Unit)

        calendarViewModel = CalendarViewModel(
            getDatesUseCase,
            getQuotesForDayUseCase,
            addQuoteUseCase,
            updateQuoteUseCases,
            deleteQuoteUseCase,
            getQuoteByIdUseCases
        )

        calendarViewModel.updateQuote(expectedDayId, testQuote)

        advanceUntilIdle()

        val dayIdCaptor = argumentCaptor<String>()
        val quoteCaptor = argumentCaptor<Quote>()

        verify(updateQuoteUseCases, times(1)).invoke(dayIdCaptor.capture(), quoteCaptor.capture())

        assertEquals(expectedDayId, dayIdCaptor.firstValue)
        assertEquals(testQuote, quoteCaptor.firstValue)
    }

    @Test
    fun `when call deleteQuote then deleteQuoteUseCase is called`(): Unit = runTest {
        val testQuote = createDays(true)[0].quotes[0]
        val expectedDayId = "1-Enero-2024"

        whenever(deleteQuoteUseCase.invoke(testQuote, expectedDayId)).thenReturn(Unit)

        calendarViewModel = CalendarViewModel(
            getDatesUseCase,
            getQuotesForDayUseCase,
            addQuoteUseCase,
            updateQuoteUseCases,
            deleteQuoteUseCase,
            getQuoteByIdUseCases
        )

        calendarViewModel.deleteQuote(testQuote, expectedDayId)

        advanceUntilIdle()

        val quoteCaptor = argumentCaptor<Quote>()
        val dayIdCaptor = argumentCaptor<String>()

        verify(deleteQuoteUseCase, times(1)).invoke(quoteCaptor.capture(), dayIdCaptor.capture())

        assertEquals(testQuote, quoteCaptor.firstValue)
        assertEquals(expectedDayId, dayIdCaptor.firstValue)
    }
}