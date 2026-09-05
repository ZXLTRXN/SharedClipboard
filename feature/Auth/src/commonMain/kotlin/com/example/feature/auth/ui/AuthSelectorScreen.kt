package com.example.feature.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.example.core.ui.glassSurface
import com.example.core.ui.composables.maxWidthButtonsTablets
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import sharedclipboard.feature.auth.generated.resources.Res
import sharedclipboard.feature.auth.generated.resources.close_ic
import sharedclipboard.feature.auth.generated.resources.createRoom
import sharedclipboard.feature.auth.generated.resources.joinRoom
import sharedclipboard.feature.auth.generated.resources.leaveRoom
import sharedclipboard.feature.auth.generated.resources.welcomeTitle
import sharedclipboard.feature.auth.generated.resources.welcomeSubtitle
import sharedclipboard.feature.auth.generated.resources.createRoomHint
import sharedclipboard.feature.auth.generated.resources.joinRoomHint

@Composable
fun AuthSelectorScreen(
    isLoggedIn: Boolean,
    onLeaveRoom: () -> Unit,
    onCreateRoom: () -> Unit,
    onJoinExistingRoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = maxWidthButtonsTablets)
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(Res.string.welcomeTitle),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = stringResource(Res.string.welcomeSubtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column(
                modifier = Modifier
                    .widthIn(max = maxWidthButtonsTablets)
                    .fillMaxWidth()
                    .glassSurface(strong = true)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onCreateRoom,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(stringResource(Res.string.createRoom), fontWeight = FontWeight.SemiBold)
                        Text(
                            stringResource(Res.string.createRoomHint),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
                Button(
                    onClick = onJoinExistingRoom,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(stringResource(Res.string.joinRoom), fontWeight = FontWeight.SemiBold)
                        Text(
                            stringResource(Res.string.joinRoomHint),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isLoggedIn,
            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
            exit = fadeOut(animationSpec = tween(400)),
            enter = fadeIn(animationSpec = tween(400)),
        ) {
            FilledTonalIconButton(
                onClick = onLeaveRoom,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                ),
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.close_ic),
                    contentDescription = stringResource(Res.string.leaveRoom),
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
