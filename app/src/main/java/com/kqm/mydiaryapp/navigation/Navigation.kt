package com.kqm.mydiaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kqm.mydiaryapp.data.CalendarRepository
import com.kqm.mydiaryapp.framework.CalendarDataSourceImpl
import com.kqm.mydiaryapp.ui.screens.CalendarScreen
import com.kqm.mydiaryapp.ui.screens.DayScreen
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import com.kqm.mydiaryapp.usecases.GetDatesUseCase

@Composable
fun Navigation() {
    val calendarDataSource = CalendarDataSourceImpl()
    val repository = CalendarRepository(calendarDataSource)
    val useCase = GetDatesUseCase(repository)
    val viewModel = CalendarViewModel(useCase)

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Calendar) {
        composable<Calendar> {
            CalendarScreen(
                viewModel = viewModel,
                onNavigateToDay = { navController.navigate(DayDetail(it)) })
        }
        composable<DayDetail> { backStackEntry ->
            val dayDetail = backStackEntry.toRoute<DayDetail>()
            DayScreen(viewModel = viewModel, day = dayDetail.day) {
                navController.popBackStack()
            }
        }
    }
}
