package com.kqm.mydiaryapp.testUnitUseCases

import com.kqm.mydiaryapp.helpers.createDays
import com.kqm.mydiaryapp.usecases.UpdateQuoteUseCases
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock

class UpdateQuoteUseCasesTest {

    @Test
    fun `invoke call function for update quote to repository`() = runBlocking {
        val quote = createDays(true)[0].quotes[0]

        val useCase = UpdateQuoteUseCases(mock { onBlocking { updateQuote("1-Enero-2024", quote) }.thenReturn(Unit) })

        assertEquals(Unit, useCase.invoke("1-Enero-2024", quote))
    }
}