package com.kqm.mydiaryapp.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kqm.mydiaryapp.data.CalendarDataSource
import com.kqm.mydiaryapp.domain.Year
import java.time.LocalDate
import javax.inject.Inject

class CalendarPagingSource @Inject constructor(private val calendarDataSourceImpl: CalendarDataSource) :
    PagingSource<Int, Year>() {

    override fun getRefreshKey(state: PagingState<Int, Year>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Year> {
        val centerYear = params.key?.takeIf { it in 1900..2100 } ?: LocalDate.now().year
        val allYears = calendarDataSourceImpl.getRangeDates(centerYear)
        val page = params.key ?: 0

        val endIndex = minOf(page + 2, allYears.size)

        return if (page < allYears.size) {
            val years = allYears.subList(page, endIndex)
            LoadResult.Page(
                data = years,
                prevKey = if (page > 1) page - 2 else null,
                nextKey = if (endIndex < allYears.size) endIndex else null
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
