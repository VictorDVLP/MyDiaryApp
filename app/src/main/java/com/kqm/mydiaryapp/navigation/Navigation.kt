package com.kqm.mydiaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kqm.mydiaryapp.ui.screens.CalendarScreen
import com.kqm.mydiaryapp.ui.screens.CreateQuoteScreen
import com.kqm.mydiaryapp.ui.screens.DayScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Calendar) {
        composable<Calendar> {
            CalendarScreen(
                onNavigateToDay = { navController.navigate(DayDetail(it)) })
        }
        composable<DayDetail> { backStackEntry ->
            val dayCalendar = backStackEntry.toRoute<DayDetail>()
            DayScreen (day = dayCalendar.dayCalendar,
               onNavigateQuote =  { navController.navigate(Quote(it)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Quote> { backStackEntry ->
            val dayDetail = backStackEntry.toRoute<Quote>()
            CreateQuoteScreen(day = dayDetail.day) { navController.popBackStack() }
        }
    }
}
