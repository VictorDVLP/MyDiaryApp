package com.kqm.mydiaryapp.data

import android.util.Log
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.framework.CalendarDataSourceImpl
import com.kqm.mydiaryapp.framework.QuoteDataSourceImpl
import com.kqm.mydiaryapp.framework.generateIdRelation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarRepository @Inject constructor(
    calendarDataSource: CalendarDataSourceImpl,
    private val quotesDataSource: QuoteDataSourceImpl
) {

    val calendarWithQuotes: Flow<Pair<List<Year>, Int>> =
        flowOf(calendarDataSource.getRangeDates())
            .flatMapLatest { calendarPair ->
                quotesDataSource.getQuotes().map { quotes ->
                    val (years, currentDay) = calendarPair
                    val quotesMap = quotes.associateBy { it.idRelation }
                    val updatedYears = years.map { year ->
                        Log.i("TAG", "calendarWithQuotes: $year")
                        year.copy(months = year.months.map { month ->
                            Log.i("TAG", "calendarWithQuotes: $month")
                            month.copy(days = month.days.map { day ->
                                val dayQuotesId = generateIdRelation(
                                    day = day.day,
                                    month = month.monthName,
                                    year = year.year
                                )
                                Log.i("TAG", "Day ID: $dayQuotesId")
                                day.copy(
                                    quotes =
                                    quotesMap[dayQuotesId]?.quotes ?: emptyList()
                                ).also { Log.i("TAG", "Day Quotes: ${day.quotes}") }
                            })
                        })
                    }
                    Pair(updatedYears, currentDay)
                }
            }

                fun quotesForDay(dayId: String): Flow<Day> = quotesDataSource.getQuotesOfDay(dayId)

                suspend fun insertQuote(day: String, quote: Quote) =
                    quotesDataSource.insertQuote(day = day, quote = quote)

                suspend fun deleteQuote(quote: Quote, day: String) =
                    quotesDataSource.deleteQuote(quote = quote, day = day)
            }