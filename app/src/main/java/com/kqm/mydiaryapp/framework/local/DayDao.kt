package com.kqm.mydiaryapp.framework.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Transaction
    @Query("SELECT * FROM days")
    fun getAllQuotes(): Flow<List<DayWithQuotes>>

    @Transaction
    @Query("SELECT * FROM days WHERE idRelation = :idRelation")
    fun getQuotesOfDay(idRelation: String): Flow<DayWithQuotes?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quote: LocalQuote)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDay(day: LocalDay)

    @Transaction
    suspend fun inserts(date: LocalDay, quotes: LocalQuote) {
        insertQuotes(quote = quotes)
        insertDay(day = date)
    }

    @Delete
    suspend fun deleteQuote(quote: LocalQuote)
}