package com.kqm.mydiaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kqm.mydiaryapp.ui.screens.CalendarScreen
import com.kqm.mydiaryapp.ui.screens.CreateQuoteScreen
import com.kqm.mydiaryapp.ui.screens.DayDetailScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = CalendarScreen ) {
        composable<CalendarScreen> {
            CalendarScreen(
                onNavigateToDay = { navController.navigate(DayDetailScreen(it)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<DayDetailScreen> { backStackEntry ->
            val dayCalendar = backStackEntry.toRoute<DayDetailScreen>()
            DayDetailScreen(
                dayId = dayCalendar.dayCalendar,
                onNavigateQuote = { dayId, quoteId ->
                    navController.navigate(
                        QuoteScreen(
                            dayId,
                            quoteId
                        )
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable<QuoteScreen> { backStackEntry ->
            val dayDetail = backStackEntry.toRoute<QuoteScreen>()
            CreateQuoteScreen(dayId = dayDetail.dayCalendar, quoteId = dayDetail.quoteId)
            { navController.popBackStack() }
        }
    }
}
