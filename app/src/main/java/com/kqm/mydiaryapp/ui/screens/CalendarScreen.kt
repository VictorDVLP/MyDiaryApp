package com.kqm.mydiaryapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    val lazyListState = rememberLazyListState()

    Scaffold { innerPadding ->
        when (state) {
            is ResultCall.Success -> { YearPager(
                years = state.value.first,
                initialPosition = state.value.second,
                onNavigateToDay = onNavigateToDay,
                paddingValues = innerPadding,
                lazyListState = lazyListState
            ) }
            is ResultCall.Loading -> { LoadingScreen() }
            is ResultCall.Error ->  { ErrorScreen(state = state, onBack = { onBack() }) }
        }
    }
}


@Composable
fun YearPager(
    years: List<Year>,
    initialPosition: Int,
    onNavigateToDay: (String) -> Unit,
    paddingValues: PaddingValues,
    lazyListState: LazyListState
) {

    val pagerState = rememberPagerState(initialPage = initialPosition / 12) { years.size }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) { page ->
        val monthIndex = initialPosition % 12
        YearView(
            year = years[page],
            onNavigateToDay = onNavigateToDay,
            modifier = Modifier.fillMaxWidth(),
            lazyListState = lazyListState,
            initialPosition = monthIndex
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
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
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
        items(months) { month ->
            MonthView(month = month, onNavigateToDay = onNavigateToDay)
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (initialPosition in 0 until lazyListState.layoutInfo.totalItemsCount) {
            lazyListState.scrollToItem(initialPosition)
        }
        focusRequester.requestFocus()
    }
}

@Composable
fun MonthView(month: Month, onNavigateToDay: (String) -> Unit, modifier: Modifier = Modifier) {
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
                .heightIn(min = 0.dp, max = 400.dp)
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
            .aspectRatio(0.875f),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.padding(4.dp)) {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day.day.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
                Spacer(modifier.padding(8.dp))
                if (quotes.isNotEmpty()) {
                    CircleTypeQuote(quoteTypeQuote = quotes)
                }
            }
        }
    }
}


@Composable
fun CircleTypeQuote(quoteTypeQuote: List<Quote>) {

    val quotesByType = remember(quoteTypeQuote) { quoteTypeQuote.groupBy { it.quoteType }.keys.toList() }

    Column {
        quotesByType.forEach { quote ->
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(12.dp)
                    .aspectRatio(1f)
                    .background(
                        color =
                        when (quote) {
                            QuoteType.PERSONAL -> Color.Green
                            QuoteType.FAMILY -> Color.Blue
                            QuoteType.FRIEND -> Color.Yellow
                            QuoteType.WORK -> Color.Red
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}
