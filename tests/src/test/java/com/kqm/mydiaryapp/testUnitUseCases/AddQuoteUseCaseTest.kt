package com.kqm.mydiaryapp.testUnitUseCases

import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.usecases.AddQuoteUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class AddQuoteUseCaseTest {

    @Test
    fun `invoke call function for add quote to repository`() = runBlocking {
        val quote = createDays(true)[0].quotes[0]

        val useCase = AddQuoteUseCase(mock { onBlocking { insertQuote("2-Enero-2024", quote) }.thenReturn(Unit) })

        assertEquals(Unit, useCase.invoke("2-Enero-2024", quote))
    }
}