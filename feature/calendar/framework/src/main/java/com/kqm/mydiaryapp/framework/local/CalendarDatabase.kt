package com.kqm.mydiaryapp.framework.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalDay::class, LocalQuote::class], version = 1, exportSchema = false)
abstract class CalendarDatabase: RoomDatabase() {
    abstract fun dayDao(): DayDao
}