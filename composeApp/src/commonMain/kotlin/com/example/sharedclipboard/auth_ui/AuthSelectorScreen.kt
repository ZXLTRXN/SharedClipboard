package com.example.sharedclipboard.auth_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.createRoom
import sharedclipboard.composeapp.generated.resources.joinRoom

@Composable
fun AuthSelectorScreen(
    onCreateRoom: () -> Unit,
    onJoinExistingRoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = onCreateRoom) {
            Text(stringResource(Res.string.createRoom))
        }
        Spacer(
            Modifier.height(16.dp)
        )
        OutlinedButton(onClick = onJoinExistingRoom) {
            Text(stringResource(Res.string.joinRoom))
        }
    }
}