package com.kqm.mydiaryapp.framework.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "quotes", primaryKeys = ["id", "idRelation"])
data class LocalQuote(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "hour") val hour: String,
    @ColumnInfo(name = "note") val quote: String,
    @ColumnInfo(name = "type") val typeQuote: String,
    @ColumnInfo(name = "idRelation") val dayId: String,
    @ColumnInfo(name = "isAlarm") val alarm: Boolean
)

@Entity(tableName = "days")
data class LocalDay(
    @PrimaryKey val idRelation: String,
)

data class DayWithQuotes(
    @Embedded val date: LocalDay,
    @Relation(
        parentColumn = "idRelation",
        entityColumn = "idRelation"
    )
    val quotes: List<LocalQuote>
)