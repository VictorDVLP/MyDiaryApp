package com.kqm.mydiaryapp.testUnitUseCases

import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.usecases.DeleteQuoteUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class DeleteQuoteUseCaseTest {

    @Test
    fun `invoke call function for delete quote from repository`() = runBlocking {
        val quote = createDays(true)[0].quotes[0]

        val useCase = DeleteQuoteUseCase( mock { onBlocking { deleteQuote(quote = quote, day = "1-Enero-2024") }.thenReturn(Unit) } )

        assertEquals(Unit, useCase.invoke(quote = quote, day = "1-Enero-2024"))
    }
}