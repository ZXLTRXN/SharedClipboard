package com.example.sharedclipboard.test

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource

@Composable
fun CounterSample(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
//        var count by mutableStateOf(0)
        var count by rememberSaveable { mutableStateOf(0) }
        val showCounterText = count > 0 // derived тут избыточен
        val showCounterButton = count < 5


        if (showCounterText) { // count > 0

            Text("Counter is $count") // stringResource()
        }

        Button(
            onClick = { count++ },
            enabled = showCounterButton, // count < 5
        ) {
            Text("Increase counter") // stringResource()
        }
    }
}