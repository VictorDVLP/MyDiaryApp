package com.kqm.mydiaryapp.framework.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "quotes")
data class LocalQuote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hour: String,
    val quote: String,
    val typeQuote: String,
    val dayId: Int
)

@Entity
data class LocalDay(
    @PrimaryKey val day: Int
)

data class DayWithQuotes(
    @Embedded val day: LocalDay,
    @Relation(
        parentColumn = "day",
        entityColumn = "dayId"
    )
    val quotes: List<LocalQuote>
)