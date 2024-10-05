package com.kqm.mydiaryapp.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.NoInspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kqm.mydiaryapp.domain.Year
import com.kqm.mydiaryapp.ui.viewmodel.ResultCall

@Composable
fun ErrorScreen(state: ResultCall<List<Year>>, onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Error icon",
            tint = Color.Red,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ups! Algo salió mal. \n${(state as ResultCall.Error).error.message}",
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onBack() },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(Color.Blue)
        ) {
            Text("Reintentar", color = Color.White)
        }
    }
}