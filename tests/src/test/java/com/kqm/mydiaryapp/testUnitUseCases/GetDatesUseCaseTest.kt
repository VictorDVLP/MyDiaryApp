package com.kqm.mydiaryapp.testUnitUseCases

import androidx.paging.PagingData
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.helpers.createYear
import com.kqm.mydiaryapp.usecases.GetDatesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class GetDatesUseCaseTest {

    @Test
    fun `invoke call function for get calendar with quotes`() {

        val expectedCalendar: Flow<PagingData<Year>> = flowOf(PagingData.from(createYear(true)))

        val useCase = GetDatesUseCase( mock { on { getCalendarWithQuotes() }.thenReturn(expectedCalendar) } )

        assertEquals(expectedCalendar, useCase())
    }
}