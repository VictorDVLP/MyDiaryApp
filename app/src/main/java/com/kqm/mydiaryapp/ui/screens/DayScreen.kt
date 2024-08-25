package com.kqm.mydiaryapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.framework.generateListQuotes
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScreen(viewModel: CalendarViewModel, day: Int, onBack: () -> Unit) {
    val quotes = generateListQuotes()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text(text = "Quotes for day $day") }
            )
        }
    ) { innerPadding ->
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(quotes) { quote ->
            QuoteItem(quote = quote)
        }
    }
    }
}


@Composable
fun QuoteItem(quote: Quote) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = quote.hour,
            fontSize = 22.sp,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = quote.note,
            fontSize = 18.sp,
            modifier = Modifier.weight(0.6f)
        )
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(18.dp)
                .aspectRatio(1f)
                .background(
                    color =
                    when (quote.quoteType) {
                        QuoteType.PERSONAL -> Color.Green
                        QuoteType.FAMILY -> Color.Blue
                        QuoteType.FRIEND -> Color.Yellow
                        QuoteType.WORK -> Color.Red
                        else -> Color.Transparent
                    },
                    shape = CircleShape
                )
        )
    }
    HorizontalDivider(
        color = Color.Gray
    )
}

@Preview(showBackground = true)
@Composable
fun QuoteItemPreview() {
    QuoteItem(quote = Quote("10:15", "Cita del Martes", QuoteType.PERSONAL))
}

@Preview(showBackground = true)
@Composable
fun QuoteListPreview() {
    val quotes = generateListQuotes()
    LazyColumn {
        items(quotes) { quote ->
            QuoteItem(quote = quote)
        }
    }
}