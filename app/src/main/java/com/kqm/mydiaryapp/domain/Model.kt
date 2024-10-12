package com.kqm.mydiaryapp.domain

data class Quote(
    val hour: String,
    val note: String,
    val quoteType: QuoteType,
    var isAlarm: Boolean = false
)

enum class QuoteType {
    FAMILIAR, PERSONAL, AMISTAD, TRABAJO
}

data class Day(
    val day: Int,
    val idRelation: String,
    var quotes: List<Quote> = emptyList()
)

data class Month(
    val monthName: String,
    val days: List<Day>,
    var offset: Int = 0
)

data class Year(
    val year: Int,
    val months: List<Month>
)