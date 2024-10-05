package com.kqm.mydiaryapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kqm.mydiaryapp.domain.Day
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.ui.screens.common.ErrorScreen
import com.kqm.mydiaryapp.ui.screens.common.LoadingScreen
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import com.kqm.mydiaryapp.ui.viewmodel.ResultCall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    dayId: String,
    onNavigateQuote: () -> Unit,
    onBack: () -> Unit
) {

    val dayStateFlow = remember { viewModel.getQuotesOfDay(dayId) }
    val day = dayStateFlow.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text(text = "Citas del dia $dayId") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateQuote() }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Quote")
            }
        }
    ) { innerPadding ->
        when (day) {
            is ResultCall.Success ->
                DayScreen(
                    day = day.value,
                    padding = innerPadding,
                    updateQuote = { onNavigateQuote() },
                    deleteQuote = viewModel::deleteQuote
                )

            is ResultCall.Loading ->
                LoadingScreen()

            is ResultCall.Error ->
                ErrorScreen(state = day, onBack = { onBack() })
        }
    }
}

@Composable
fun DayScreen(
    day: Day,
    padding: PaddingValues,
    updateQuote: () -> Unit,
    deleteQuote: (Quote, String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(padding)) {
        items(day.quotes) { quote ->
            QuoteItem(
                quote = quote,
                onUpdateClick = { updateQuote() },
                onDeleteClick = { deleteQuote(quote, day.idRelation) }
            )
        }
    }
}


@Composable
fun QuoteItem(quote: Quote, onUpdateClick: () -> Unit, onDeleteClick: (Quote) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = quote.hour,
            fontSize = 19.sp,
            modifier = Modifier.weight(0.25f)
        )
        Text(
            text = quote.note,
            fontSize = 16.sp,
            modifier = Modifier.weight(0.6f)
        )
        IconButton(modifier = Modifier.weight(0.1f), onClick = { onUpdateClick() }) {
            Icon(Icons.Filled.Create, contentDescription = "Add Quote")
        }
        IconButton(modifier = Modifier.weight(0.1f), onClick = { onDeleteClick(quote) }) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete Quote")
        }
        Box(
            modifier = Modifier
                .weight(0.07f)
                .padding(2.dp)
                .aspectRatio(1f)
                .background(
                    color =
                    when (quote.quoteType) {
                        QuoteType.PERSONAL -> Color.Green
                        QuoteType.FAMILY -> Color.Blue
                        QuoteType.FRIEND -> Color.Yellow
                        QuoteType.WORK -> Color.Red
                    },
                    shape = CircleShape
                )
        )
    }
    HorizontalDivider(
        color = Color.Gray
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DayScreenPreview() {
    QuoteItem( quote = Quote("10:00", "Cita 1", QuoteType.PERSONAL), onUpdateClick = {}, onDeleteClick = {})
}
