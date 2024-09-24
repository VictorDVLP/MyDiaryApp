package com.kqm.mydiaryapp.domain

data class Quote(val hour: String, val note: String, val quoteType: QuoteType)

enum class QuoteType {
    FAMILY, PERSONAL, FRIEND, WORK
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