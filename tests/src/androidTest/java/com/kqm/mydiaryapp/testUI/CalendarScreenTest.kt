package com.kqm.mydiaryapp.testUI

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.helpers.createYear
import com.kqm.mydiaryapp.ui.screens.CalendarScreen
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CalendarScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenInitializeCalendar_showCalendar(): Unit = with(composeTestRule) {
        setContent {
            val pagingData = flowOf(PagingData.from(createYear(true)))
            CalendarScreen(
                lazyPagingItems = pagingData.collectAsLazyPagingItems(),
                currentMonthIndex = 0,
                onNavigateToDay = {},
                onBack = {}
            )
        }

        onNodeWithTag("CalendarScreen").assertIsDisplayed()
    }

    @Test
    fun whenInitializeCalendar_returnErrorShowMessageError(): Unit = with(composeTestRule) {
        val fakePagingSource = object : PagingSource<Int, Year>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Year> {
                return LoadResult.Error(Exception("Error de paginación"))
            }
            override fun getRefreshKey(state: PagingState<Int, Year>): Int? = null
        }
        val pagingData = Pager(PagingConfig(pageSize = 2)) { fakePagingSource }.flow

        setContent {
            val lazyPagingItems = pagingData.collectAsLazyPagingItems()

            CalendarScreen(
                lazyPagingItems = lazyPagingItems,
                currentMonthIndex = 0,
                onNavigateToDay = {},
                onBack = {}
            )
        }

        onNodeWithTag("ErrorScreen").assertIsDisplayed()
    }

    @Test
    fun whenClickOnDay_callOnNavigateToDay(): Unit = with(composeTestRule) {
        var keyNavigate = ""
        setContent {
            val pagingData = flowOf(PagingData.from(createYear(true)))
            CalendarScreen(
                lazyPagingItems = pagingData.collectAsLazyPagingItems(),
                currentMonthIndex = 0,
                onNavigateToDay = {keyNavigate = it},
                onBack = {}
            )
        }

        onNodeWithTag("CellDay").performClick()
        assertEquals(keyNavigate, "1-Enero-2024")
    }

    @Test
    fun whenClickOnBack_callOnBack(): Unit = with(composeTestRule) {
        val fakePagingSource = object : PagingSource<Int, Year>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Year> {
                return LoadResult.Error(Exception("Error de paginación"))
            }
            override fun getRefreshKey(state: PagingState<Int, Year>): Int? = null
        }
        val pagingData = Pager(PagingConfig(pageSize = 2)) { fakePagingSource }.flow
        var keyBack = false

        setContent {
            CalendarScreen(
                lazyPagingItems = pagingData.collectAsLazyPagingItems(),
                currentMonthIndex = 0,
                onNavigateToDay = {},
                onBack = {keyBack = true}
            )
        }
        onNodeWithTag("Back").performClick()
        assertEquals(keyBack, true)
    }
}