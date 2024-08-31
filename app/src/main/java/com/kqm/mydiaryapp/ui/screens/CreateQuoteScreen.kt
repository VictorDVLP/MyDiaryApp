package com.kqm.mydiaryapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuoteScreen(viewModel: CalendarViewModel, day: Int, onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf(QuoteType.WORK) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Crear evento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(
                state = TimePickerState(initialHour = 5, initialMinute = 0, is24Hour = true),
                colors = TimePickerDefaults.colors(
                    containerColor = Color.Blue,
                    clockDialColor = Color.White
                ),
                layoutType = TimePickerLayoutType.Vertical,
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = "",
                onValueChange = { text = it },
                label = { Text("Evento, cita, reunión...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Text(text = "Selecciona tipo:", fontSize = 16.sp)

                Spacer(Modifier.padding(vertical = 1.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QuoteType.entries.forEach { quoteType ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RadioButton(
                                selected = selectedOption == quoteType,
                                onClick = { selectedOption = quoteType }
                            )
                            Text(
                                text = quoteType.name,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Visible,
                                fontStyle = FontStyle.Italic,
                                fontWeight = if (selectedOption == quoteType) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = { /*TODO*/ },
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Guardar", fontSize = 16.sp)
                }
            }
        }
    }
}
