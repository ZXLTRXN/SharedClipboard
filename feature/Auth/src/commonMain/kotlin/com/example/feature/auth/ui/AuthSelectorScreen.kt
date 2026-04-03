package com.example.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.ui.composables.errorOutlinedButtonColors
import org.jetbrains.compose.resources.stringResource
import sharedclipboard.feature.auth.generated.resources.Res
import sharedclipboard.feature.auth.generated.resources.createRoom
import sharedclipboard.feature.auth.generated.resources.joinRoom
import sharedclipboard.feature.auth.generated.resources.leaveRoom

@Composable
fun AuthSelectorScreen(
    isLoggedIn: Boolean,
    onLeaveRoom: () -> Unit,
    onCreateRoom: () -> Unit,
    onJoinExistingRoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (isLoggedIn) {
            OutlinedButton(onClick = onLeaveRoom,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.errorOutlinedButtonColors()) {
                Text(stringResource(Res.string.leaveRoom))
            }
            Spacer(
                Modifier.height(16.dp)
            )
        }

        OutlinedButton(onClick = onCreateRoom, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.createRoom))
        }
        Spacer(
            Modifier.height(16.dp)
        )
        Button(onClick = onJoinExistingRoom, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.joinRoom))
        }
    }
}