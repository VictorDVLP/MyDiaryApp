package com.kqm.mydiaryapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kqm.mydiaryapp.domain.Quote
import com.kqm.mydiaryapp.domain.QuoteType
import com.kqm.mydiaryapp.notification.startNotification
import com.kqm.mydiaryapp.ui.viewmodel.CalendarViewModel
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuoteScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    dayId: String,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val timeState = remember { mutableStateOf(LocalTime.of(5, 0)) }
    val timePickerState = rememberTimePickerState(
        initialHour = timeState.value.hour,
        initialMinute = timeState.value.minute,
        is24Hour = true
    )
    val hour = timePickerState.hour
    val minute = timePickerState.minute
    val time = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
    val textState = remember { mutableStateOf("") }
    val selectedQuoteType = remember { mutableStateOf(QuoteType.TRABAJO) }
    val alarm = remember { mutableStateOf(false) }

    val quote = Quote(
        hour = time,
        note = textState.value,
        quoteType = selectedQuoteType.value,
        isAlarm = alarm.value
    )

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
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    containerColor = Color.Blue,
                    clockDialColor = Color.White
                ),
                layoutType = TimePickerLayoutType.Vertical,
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                label = { Text("Evento, cita, reunión...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Recordarme este evento 24hs antes:", fontSize = 16.sp)
                Checkbox(checked = alarm.value, onCheckedChange = { alarm.value = it })
            }

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
                                selected = selectedQuoteType.value == quoteType,
                                onClick = { selectedQuoteType.value = quoteType }
                            )
                            Text(
                                text = quoteType.name,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Visible,
                                fontStyle = FontStyle.Italic,
                                fontWeight = if (selectedQuoteType.value == quoteType) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = {
                        viewModel.addQuote(dayId, quote)
                        if (alarm.value) {
                            startNotification(context, dayId, timePickerState, textState.value)
                        }
                    },
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Guardar", fontSize = 16.sp)
                }
            }
        }
    }
}
