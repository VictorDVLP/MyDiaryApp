package com.kqm.mydiaryapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.kqm.mydiaryapp.ui.viewmodel.ResultCall
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuoteScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    dayId: String,
    quoteId: Int?,
    onBack: () -> Unit
) {

    val state = rememberCreateQuoteState(dayId, quoteId)

    val isUpdating = quoteId != null

    val context = LocalContext.current

    val timePickerState = rememberTimePickerState(
        initialHour = state.timeState.hour,
        initialMinute = state.timeState.minute,
        is24Hour = true
    )

    UpdateQuoteState(viewModel, dayId, quoteId, timePickerState, state)

    val quote = Quote(
        id = state.idState,
        hour = "${timePickerState.hour.toString().padStart(2, '0')
        }:${timePickerState.minute.toString().padStart(2, '0')}",
        note = state.textState,
        quoteType = state.selectedQuoteType,
        isAlarm = state.alarm
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
                value = state.textState,
                onValueChange = { state.textState = it },
                label = { Text("Evento, cita, reunión...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Recordarme este evento 24hs antes:", fontSize = 16.sp)
                Switch(
                    checked = state.alarm,
                    onCheckedChange = { state.alarm = it },
                    thumbContent = if (state.alarm) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Alarm active",
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
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
                                selected = state.selectedQuoteType == quoteType,
                                onClick = { state.selectedQuoteType = quoteType }
                            )
                            Text(
                                text = quoteType.name,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Visible,
                                fontStyle = FontStyle.Italic,
                                fontWeight = if (state.selectedQuoteType == quoteType) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = {
                        if (isUpdating) {
                            viewModel.updateQuote(
                                dayId,
                                quote
                            )
                        } else {
                            viewModel.addQuote(dayId, quote)
                        }
                        if (state.alarm) {
                            startNotification(context, dayId, timePickerState, state.textState)
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
