package com.kqm.mydiaryapp.framework.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Transaction
    @Query("SELECT * FROM days")
    fun getAllQuotes(): Flow<List<DayWithQuotes>>

    @Query("SELECT MAX(id) FROM quotes WHERE idRelation = :dayId")
    fun getLastQuotesIdForDay(dayId: String): Flow<Int>

    @Query("SELECT * FROM quotes WHERE id = :id AND idRelation = :idRelation")
    fun getQuoteById(id: Int, idRelation: String): Flow<LocalQuote>

    @Transaction
    @Query("SELECT * FROM days WHERE idRelation = :idRelation")
    fun getQuotesOfDay(idRelation: String): Flow<DayWithQuotes?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quote: LocalQuote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: LocalDay)

    @Transaction
    suspend fun inserts(date: LocalDay, quotes: LocalQuote) {
        insertQuotes(quote = quotes)
        insertDay(day = date)
    }

    @Update
    suspend fun updateQuote(quote: LocalQuote)

    @Delete
    suspend fun deleteQuote(quote: LocalQuote)
}