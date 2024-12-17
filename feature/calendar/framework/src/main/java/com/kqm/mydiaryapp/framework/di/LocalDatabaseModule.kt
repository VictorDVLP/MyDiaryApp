package com.kqm.mydiaryapp.framework.di

import android.app.Application
import androidx.room.Room
import com.kqm.mydiaryapp.data.CalendarDataSource
import com.kqm.mydiaryapp.data.QuoteDataSource
import com.kqm.mydiaryapp.framework.CalendarDataSourceImpl
import com.kqm.mydiaryapp.framework.QuoteDataSourceImpl
import com.kqm.mydiaryapp.framework.local.CalendarDatabase
import com.kqm.mydiaryapp.framework.local.DayDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {

    @Singleton
    @Provides
    fun provideDao(db: CalendarDatabase): DayDao = db.dayDao()

    @Singleton
    @Provides
    fun provideDatabase(application: Application): CalendarDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = CalendarDatabase::class.java,
            name = "my-diary-db"
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindQuoteDataSource(quoteDataSource: QuoteDataSourceImpl): QuoteDataSource

    @Binds
    abstract fun bindCalendarDataSource(calendarDataSource: CalendarDataSourceImpl): CalendarDataSource
}