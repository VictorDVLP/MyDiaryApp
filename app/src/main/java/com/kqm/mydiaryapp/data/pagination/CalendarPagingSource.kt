package com.kqm.mydiaryapp.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.framework.CalendarDataSourceImpl
import javax.inject.Inject

class CalendarPagingSource @Inject constructor(private val calendarDataSourceImpl: CalendarDataSourceImpl) :
    PagingSource<Int, Year>() {

    override fun getRefreshKey(state: PagingState<Int, Year>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Year> {
        val allYears = calendarDataSourceImpl.getRangeDates().first
        val yearInitial = calendarDataSourceImpl.getRangeDates().second
        val page = params.key ?: yearInitial

        return if (page in allYears.indices) {
            val year = allYears[page]
            LoadResult.Page(
                data = listOf(year),
                prevKey = if (page > 0) page - 1 else null,
                nextKey = if (page < allYears.size - 1) page + 1 else null
            )
        } else {
            LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
    }
}
