package com.kqm.mydiaryapp.ui.viewmodel

import android.database.sqlite.SQLiteException
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface ResultCall<out T> {
    data class Success<out T>(val value: T) : ResultCall<T>
    data class Error(val error: Throwable) : ResultCall<Nothing>
    data object Loading : ResultCall<Nothing>
}

fun <T> Flow<T>.stateAsResultIn(coroutineScope: CoroutineScope): StateFlow<ResultCall<T>> =
        map<T, ResultCall<T>> { ResultCall.Success(it) }
        .handleError()
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResultCall.Loading
        )

private fun <T> Flow<ResultCall<T>>.handleError(): Flow<ResultCall<T>> =
    catch { e ->
        Log.i("TAG", "handleError: ${e.message} and ${e.cause}")
        val errorMessage = when (e) {
            is SQLiteException -> "Database error"
            is RuntimeException -> "Timeout error"
            else -> "Unknown error"
        }
        emit(ResultCall.Error(Exception(errorMessage)))
    }