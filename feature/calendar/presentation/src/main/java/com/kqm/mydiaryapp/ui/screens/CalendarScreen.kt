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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Month
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType.AMISTAD
import com.kqm.mydiaryapp.domain.QuoteType.FAMILIAR
import com.kqm.mydiaryapp.domain.QuoteType.PERSONAL
import com.kqm.mydiaryapp.domain.QuoteType.TRABAJO
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.ui.screens.common.ErrorScreen
import com.kqm.mydiaryapp.ui.screens.common.LoadingScreen
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import java.time.LocalDate

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onNavigateToDay: (String) -> Unit,
    onBack: () -> Unit
) {
    val lazyPagingItems = viewModel.calendarWithQuotes.collectAsLazyPagingItems()
    val currentMonthIndex = LocalDate.now().month.value - 1

    CalendarScreen(
        lazyPagingItems = lazyPagingItems,
        currentMonthIndex = currentMonthIndex,
        onNavigateToDay = onNavigateToDay,
        onBack = onBack
    )
}

@Composable
fun CalendarScreen(
    lazyPagingItems: LazyPagingItems<Year>,
    currentMonthIndex: Int,
    onNavigateToDay: (String) -> Unit,
    onBack: () -> Unit
) {

    Scaffold(
        modifier = Modifier.
        testTag("CalendarScreen")
    ) { innerPadding ->
        when (lazyPagingItems.loadState.refresh) {

            is LoadState.Error -> {
                val errorState = lazyPagingItems.loadState.refresh as LoadState.Error
                ErrorScreen(state = errorState, onBack = { onBack() })
            }

            else -> {
                YearPager(
                    lazyPagingItems = lazyPagingItems,
                    positionMonth = currentMonthIndex,
                    onNavigateToDay = onNavigateToDay,
                    paddingValues = innerPadding
                )
            }
        }
    }
}


@Composable
fun YearPager(
    lazyPagingItems: LazyPagingItems<Year>,
    positionMonth: Int,
    onNavigateToDay: (String) -> Unit,
    paddingValues: PaddingValues,
) {

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { lazyPagingItems.itemCount })
    val lazyListStates = remember { mutableStateMapOf<Int, LazyListState>() }

    HorizontalPager(
        state = pagerState,
        beyondViewportPageCount = 2,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) { page ->
        val year = lazyPagingItems[page]
        if (year != null) {

            val lazyListState = lazyListStates.getOrPut(page) {
                rememberLazyListState()
            }
            YearView(
                year = year,
                onNavigateToDay = onNavigateToDay,
                modifier = Modifier.fillMaxWidth(),
                lazyListState = lazyListState,
                initialMonth = positionMonth
            )
        } else {
            LoadingScreen()
        }

    }
}

@Composable
fun YearView(
    year: Year,
    onNavigateToDay: (String) -> Unit,
    lazyListState: LazyListState,
    initialMonth: Int,
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
                modifier = modifier
            )

            LaunchedEffect(key1 = initialMonth) {
                if (initialMonth < year.months.size && year.year == LocalDate.now().year) {
                    lazyListState.scrollToItem(initialMonth)
                }
            }
        }
}

@Composable
fun MonthList(
    months: List<Month>,
    onNavigateToDay: (String) -> Unit,
    lazyListState: LazyListState,
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
            MonthView(
                month = month,
                onNavigateToDay = onNavigateToDay
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
        }
    }
}

@Composable
fun MonthView(
    month: Month,
    onNavigateToDay: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
    onDayClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onDayClick(day.idRelation) },
        modifier = modifier
            .padding(1.dp)
            .height(90.dp)
            .testTag("CellDay"),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (day.isCurrentDay) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = day.day.toString(),
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Start
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
                        PERSONAL -> Color.Green
                        FAMILIAR -> Color.Blue
                        AMISTAD -> Color.Yellow
                        TRABAJO -> Color.Red
                    },
                    radius = size.minDimension / 2f,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            }
        }
    }
}
