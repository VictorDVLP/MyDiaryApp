package com.kqm.mydiaryapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object Calendar

@Serializable
data class DayDetail(val dayCalendar: Int)

@Serializable
data class Quote(val day: Int)

@Serializable
object News

