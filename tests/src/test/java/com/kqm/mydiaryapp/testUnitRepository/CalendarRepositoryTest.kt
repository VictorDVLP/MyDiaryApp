package com.kqm.mydiaryapp.testUnitRepository

import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.data.pagination.CalendarPagingSource
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.framework.QuoteDataSourceImpl
import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.helpers.createYear
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CalendarRepositoryTest {

    @Mock
    private lateinit var calendarPagingSource: CalendarPagingSource

    @Mock
    private lateinit var quoteDataSource: QuoteDataSourceImpl


    private lateinit var calendarRepository: CalendarRepository


    @Before
    fun setUp() {
        calendarRepository = CalendarRepository(calendarPagingSource, quoteDataSource)
    }


    @Test
    fun `getCalendarWithQuotes should call return a calendar with quotes`() = runTest {

        val calendarBasic = createYear(false)
        val expectedCalendar = createYear(true)

        whenever(calendarPagingSource.load(any())).thenReturn(
            PagingSource.LoadResult.Page(
                calendarBasic,
                null,
                null
            )
        )
        whenever(quoteDataSource.getQuotes()).thenReturn(flowOf(createDays(true)))

        val result = calendarRepository.getCalendarWithQuotes().asSnapshot()

        assertEquals(expectedCalendar, result)

        verify(calendarPagingSource).load(any())
        verify(quoteDataSource).getQuotes()
    }

    @Test
    fun `quotesForDay should call getQuotesOfDay from QuoteDataSource and return a Day`() =
        runTest {

            val expectedDay: Day = createDays(true)[0]

            whenever(quoteDataSource.getQuotesOfDay(any())).thenReturn(flowOf(expectedDay))

            val actualDay = calendarRepository.quotesForDay("1-Enero-2024").toList()[0]
            assertEquals(expectedDay, actualDay)

            val dateCaptor = argumentCaptor<String>()

            verify(quoteDataSource).getQuotesOfDay(dateCaptor.capture())
            assertEquals("1-Enero-2024", dateCaptor.firstValue)
        }

    @Test
    fun `quoteForId should call getQuoteById from QuoteDataSource and return a Quote`() = runTest {

        val expectedQuote = createDays(true)[0].quotes[0]

        whenever(quoteDataSource.getQuoteById(any(), any())).thenReturn(flowOf(expectedQuote))

        val actualQuote = calendarRepository.getQuoteById(1, "1-Enero-2024").toList()[0]
        assertEquals(expectedQuote, actualQuote)

        val idCaptor = argumentCaptor<Int>()
        val dateCaptor = argumentCaptor<String>()

        verify(quoteDataSource).getQuoteById(idCaptor.capture(), dateCaptor.capture())
        assertEquals(1, idCaptor.firstValue)
        assertEquals("1-Enero-2024", dateCaptor.firstValue)
    }

    @Test
    fun `insertQuote should call insertQuote from QuoteDataSource and insert quote in database`() =
        runTest {

            val quote = Quote(3, "15:00", "Comida familiar", QuoteType.FAMILIAR, false)

            whenever(quoteDataSource.insertQuote(any(), any())).thenReturn(Unit)

            calendarRepository.insertQuote("1-Enero-2024", quote)
            val dateCaptor = argumentCaptor<String>()
            val quoteCaptor = argumentCaptor<Quote>()

            verify(quoteDataSource).insertQuote(dateCaptor.capture(), quoteCaptor.capture())

            assertEquals("1-Enero-2024", dateCaptor.firstValue)
            assertEquals(quote, quoteCaptor.firstValue)
        }

    @Test
    fun `updateQuote should call updateQuote from QuoteDataSource and update quote in database`() =
        runTest {

            val quote = Quote(1, "13:45", "Dentista", QuoteType.PERSONAL, true)

            whenever(quoteDataSource.updateQuote(any(), any())).thenReturn(Unit)

            calendarRepository.updateQuote("1-Enero-2024", quote)
            val dateCaptor = argumentCaptor<String>()
            val quoteCaptor = argumentCaptor<Quote>()

            verify(quoteDataSource).updateQuote( dateCaptor.capture(), quoteCaptor.capture())

            assertEquals("1-Enero-2024", dateCaptor.firstValue)
            assertEquals(quote, quoteCaptor.firstValue)
        }

    @Test
    fun `deleteQuote should call deleteQuote from QuoteDataSource and delete quote in database`() =
        runTest {

            val quote = Quote(3, "15:30", "Comida familiar", QuoteType.FAMILIAR, false)

            whenever(quoteDataSource.deleteQuote(any(), any())).thenReturn(Unit)

            calendarRepository.deleteQuote(quote, "1-Enero-2024")
            val dateCaptor = argumentCaptor<String>()
            val quoteCaptor = argumentCaptor<Quote>()

            verify(quoteDataSource).deleteQuote(quoteCaptor.capture(), dateCaptor.capture())

            assertEquals(quote, quoteCaptor.firstValue)
            assertEquals("1-Enero-2024", dateCaptor.firstValue)
        }
}
