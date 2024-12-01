package com.kqm.mydiaryapp.testUnitPagination

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import com.kqm.mydiaryapp.data.pagination.CalendarPagingSource
import com.kqm.mydiaryapp.framework.CalendarDataSourceImpl
import com.kqm.mydiaryapp.helpers.createYear
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CalendarPagingSourceTest {

    @Mock
    private lateinit var calendarDataSource: CalendarDataSourceImpl

    private lateinit var calendarPagingSourceTest: CalendarPagingSource

    @Before
    fun setUp() {
        calendarPagingSourceTest = CalendarPagingSource(calendarDataSource)
    }

    @Test
    fun `load returns a page when on success full load of item keyed data`() = runTest {

        val yearTest = createYear(false)
        whenever(calendarDataSource. getRangeDates( ) ) . thenReturn( Pair( yearTest, 1))

        val result = LoadResult.Page(data = yearTest.subList(0, 2), prevKey = null, nextKey = 2)

        assertEquals(result,  calendarPagingSourceTest. load( PagingSource. LoadParams. Refresh( 0,  1, false)))
        verify(calendarDataSource,  times(1)).getRangeDates( )

    }

    @Test
    fun `load returns next page when given a next key`() = runTest {

        val yearTest = createYear(false)
        whenever(calendarDataSource. getRangeDates( ) ) . thenReturn( Pair( yearTest, 1))

        calendarPagingSourceTest.load( PagingSource.LoadParams.Refresh( 0,  1, false))

        val result = calendarPagingSourceTest.load( PagingSource.LoadParams.Append( 2,  1, false))

        val expectData = yearTest.subList(2, 3)

        val expectedResult = LoadResult.Page(data = expectData, prevKey = 0, nextKey = null)

        assertEquals(expectedResult, result)
        verify(calendarDataSource, times(2)).getRangeDates( )
    }

}