package com.kqm.mydiaryapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object CalendarScreen

@Serializable
data class DayDetailScreen(val dayCalendar: String)

@Serializable
data class QuoteScreen(val dayCalendar: String, val quoteId: Int?)

@Serializable
object NewsScreen

