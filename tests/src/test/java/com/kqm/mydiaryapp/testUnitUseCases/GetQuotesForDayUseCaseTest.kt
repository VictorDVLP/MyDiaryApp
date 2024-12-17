package com.kqm.mydiaryapp.testUnitUseCases

import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.usecases.GetQuotesForDayUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class GetQuotesForDayUseCaseTest {

    @Test
    fun `invoke call function quotes for day from repository`() {

        val quotesForDayFlow: Flow<Day> = flowOf(createDays(true)[1])

        val useCase = GetQuotesForDayUseCase(mock {
            on { quotesForDay("2-Enero-2024") }.thenReturn(quotesForDayFlow)
        })

        assertEquals(quotesForDayFlow, useCase.invoke("2-Enero-2024"))
    }
}
