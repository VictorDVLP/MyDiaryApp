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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqm.mydiaryapp.domain.Month
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel

@Composable
fun CalendarScreen(viewModel: CalendarViewModel, onNavigateToDay: (Int) -> Unit) {

    val dates = viewModel.getCalendar.value
    val position = viewModel.initialPosition.value

    val lazyListState = rememberLazyListState()

    Scaffold { innerPadding ->
        YearPager(
            years = dates,
            initialPosition = position,
            onNavigateToDay = onNavigateToDay,
            paddingValues = innerPadding,
            lazyListState = lazyListState
        )
    }
}


@Composable
fun YearPager(
    years: List<Year>,
    initialPosition: Int,
    onNavigateToDay: (Int) -> Unit,
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
    onNavigateToDay: (Int) -> Unit,
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
    onNavigateToDay: (Int) -> Unit,
    lazyListState: LazyListState,
    initialPosition: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = lazyListState
    ) {
        items(months) { month ->
            MonthView(month = month, onNavigateToDay = onNavigateToDay)
        }
    }

    LaunchedEffect(key1 = months) {
        if (initialPosition in 0 until lazyListState.layoutInfo.totalItemsCount) {
            lazyListState.scrollToItem(initialPosition)
        }
    }
}

@Composable
fun MonthView(month: Month, onNavigateToDay: (Int) -> Unit, modifier: Modifier = Modifier) {
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

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items(listOf("L", "M", "X", "J", "V", "S", "D")) { letra ->
                Text(text = letra, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

            items(month.days) { day ->
                CellDay(
                    day = day.day,
                    quotes = emptyList(),
                    onDayClick = { onNavigateToDay(day.day) }
                )
            }
        }
    }
}

@Composable
fun CellDay(
    day: Int,
    quotes: List<Quote>,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onDayClick(day) },
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
                    text = day.toString(),
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

    val quotesByType = quoteTypeQuote.groupBy { it.quoteType }.keys.toList()

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
