package com.kqm.mydiaryapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Month
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.ui.screens.common.ErrorScreen
import com.kqm.mydiaryapp.ui.screens.common.LoadingScreen
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import com.kqm.mydiaryapp.ui.viewmodel.ResultCall

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onNavigateToDay: (String) -> Unit,
    onBack: () -> Unit
) {

    val state = viewModel.calendarWithQuotes.collectAsState().value

    Scaffold { innerPadding ->
        when (state) {
            is ResultCall.Success -> YearPager(
                years = state.value.first,
                initialPosition = state.value.second,
                onNavigateToDay = onNavigateToDay,
                paddingValues = innerPadding
            )

            is ResultCall.Loading -> LoadingScreen()
            is ResultCall.Error -> ErrorScreen(state = state, onBack = { onBack() })
        }
    }
}


@Composable
fun YearPager(
    years: List<Year>,
    initialPosition: Int,
    onNavigateToDay: (String) -> Unit,
    paddingValues: PaddingValues,
) {

    val pagerState = rememberPagerState(initialPage = initialPosition / 12) { years.size }
    val lazyListStates = remember { mutableStateMapOf<Int, LazyListState>() }


    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) { page ->
        val lazyListState = lazyListStates.getOrPut(page) {
            rememberLazyListState()
        }
        YearView(
            year = years[page],
            onNavigateToDay = onNavigateToDay,
            modifier = Modifier.fillMaxWidth(),
            lazyListState = lazyListState,
            initialPosition = initialPosition % 12
        )
    }
}

@Composable
fun YearView(
    year: Year,
    onNavigateToDay: (String) -> Unit,
    lazyListState: LazyListState,
    initialPosition: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = year.year.toString(),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        MonthList(
            months = year.months,
            onNavigateToDay = onNavigateToDay,
            lazyListState = lazyListState,
            initialPosition = initialPosition,
            modifier = modifier
        )

        LaunchedEffect(Unit) {
            if (initialPosition < year.months.size) {
               lazyListState.scrollToItem(initialPosition)
            }
        }
    }
}

@Composable
fun MonthList(
    months: List<Month>,
    onNavigateToDay: (String) -> Unit,
    lazyListState: LazyListState,
    initialPosition: Int,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .focusRequester(focusRequester),
        state = lazyListState
    ) {
        items(months, key = { it.monthName }) { month ->
            MonthView(month = month, initialPosition = initialPosition, onNavigateToDay = onNavigateToDay)
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
        }
    }
}

@Composable
fun MonthView(month: Month, initialPosition: Int, onNavigateToDay: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp, top = 24.dp, bottom = 12.dp)
    ) {
        Text(
            text = month.monthName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { letter ->
                Text(text = letter, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 0.dp, max = 600.dp)
        ) {
            items(month.offset) {
                Box(modifier = Modifier.size(40.dp))
            }

            items(month.days, key = { it.idRelation }) { day ->
                CellDay(
                    day = day,
                    quotes = day.quotes,
                    initialPosition = initialPosition,
                    onDayClick = { onNavigateToDay(day.idRelation) }
                )
            }
        }
    }
}

@Composable
fun CellDay(
    day: Day,
    quotes: List<Quote>,
    initialPosition: Int,
    onDayClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onDayClick(day.idRelation) },
        modifier = modifier
            .padding(1.dp)
            .height(90.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Text(
                text = day.day.toString(),
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )
            if (quotes.isNotEmpty()) {
                CircleTypeQuote(
                    quoteTypeQuote = quotes
                )
            }
        }
        }
    }
}


@Composable
fun CircleTypeQuote(quoteTypeQuote: List<Quote>) {

    val quotesByType =
        remember(quoteTypeQuote) { quoteTypeQuote.groupBy { it.quoteType }.keys.toList() }

    Column {
        quotesByType.forEach { quote ->
            Canvas(
                modifier = Modifier
                    .size(14.dp)
                    .padding(2.dp)
                    .aspectRatio(1f)
            ) {
                drawCircle(
                    color = when (quote) {
                        QuoteType.PERSONAL -> Color.Green
                        QuoteType.FAMILY -> Color.Blue
                        QuoteType.FRIEND -> Color.Yellow
                        QuoteType.WORK -> Color.Red
                    },
                    radius = size.minDimension / 2f,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MonthScreenPreview() {
    MonthView(
        Month(
            "Enero",
            listOf(
                Day(
                    1,
                    "1-Enero-2024",
                    listOf(
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.PERSONAL),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.WORK),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FAMILY),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FRIEND)
                    )
                ),
                Day(
                    2, "2-Enero-2024", listOf(
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.PERSONAL),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.WORK),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FAMILY),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FRIEND)
                    )
                ),
                Day(
                    3, "3-Enero-2024", listOf(
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.PERSONAL),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.WORK),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FAMILY),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FRIEND)
                    )
                ),
                Day(4, "4-Enero-2024"),
                Day(5, "5-Enero-2024"),
                Day(6, "6-Enero-2024"),
                Day(7, "7-Enero-2024"),
                Day(
                    8, "8-Enero-2024", listOf(
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.PERSONAL),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.WORK),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FAMILY),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FRIEND)
                    )
                ),
                Day(9, "9-Enero-2024"),
                Day(
                    10, "10-Enero-2024", listOf(
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.PERSONAL),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.WORK),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FAMILY),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FRIEND)
                    )
                ),
                Day(11, "11-Enero-2024"),
                Day(12, "12-Enero-2024"),
                Day(13, "13-Enero-2024"),
                Day(14, "14-Enero-2024"),
                Day(15, "15-Enero-2024"),
                Day(16, "16-Enero-2024"),
                Day(17, "17-Enero-2024"),
                Day(18, "18-Enero-2024"),
                Day(19, "19-Enero-2024"),
                Day(
                    20, "20-Enero-2024", listOf(
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.PERSONAL),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.WORK),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FAMILY),
                        Quote(hour = "10:00", note = "Hola", quoteType = QuoteType.FRIEND)
                    )
                ),
                Day(21, "21-Enero-2024"),
                Day(22, "22-Enero-2024"),
                Day(23, "23-Enero-2024"),
                Day(24, "24-Enero-2024"),
                Day(25, "25-Enero-2024"),
                Day(26, "26-Enero-2024"),
                Day(27, "27-Enero-2024"),
                Day(28, "28-Enero-2024"),
                Day(29, "29-Enero-2024"),
                Day(30, "30-Enero-2024"),
                Day(31, "31-Enero-2024"),
            )
        ), 2 ,{})
}
