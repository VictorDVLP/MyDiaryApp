package com.kqm.mydiaryapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object Calendar

@Serializable
data class DayDetail(val day: Int)

@Serializable
object Quote

@Serializable
object News

