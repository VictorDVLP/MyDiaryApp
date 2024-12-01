package com.kqm.mydiaryapp.testUnitUseCases

import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.usecases.GetQuoteByIdUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock

class GetQuoteByIdUseCasesTest {

    @Test
    fun `invoke call function for get quote by id from repository`() {
        val quoteFlow: Flow<Quote> = flowOf(createDays(true)[2].quotes[1])

        val useCase = GetQuoteByIdUseCases(mock {
            on { getQuoteById(2, "3-Enero-2024") }.thenReturn(quoteFlow)
        })

        assertEquals(quoteFlow, useCase.invoke(2, "3-Enero-2024"))
    }
}