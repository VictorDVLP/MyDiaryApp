package com.kqm.mydiaryapp.framework.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Transaction
    @Query("SELECT * FROM quotes")
    fun getQuotes(): Flow<List<DayWithQuotes>>

    @Insert
    suspend fun insertQuotes(quote: List<LocalQuote>)

    @Insert
    suspend fun insertDay(day: LocalDay)

    @Transaction
    suspend fun inserts(quotes: DayWithQuotes) {
        insertQuotes(quote = quotes.quotes)
        insertDay(day = quotes.day)
    }

    @Delete
    suspend fun deleteDay(quote: LocalQuote)
}