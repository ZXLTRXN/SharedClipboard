package com.example.sharedclipboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.firebaseapi.domain.ClipboardRepository
import com.example.sharedclipboard.di.initKoin
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Shared Clipboard",
        state = rememberWindowState(
            width = 520.dp,
            height = 680.dp
        )
    ) {
        var startupResult by remember { mutableStateOf<Result<Unit>?>(null) }

        LaunchedEffect(Unit) {
            startupResult = initializeInBackground()
        }

        when {
            startupResult == null -> StartupScreen()
            startupResult?.isSuccess == true -> App()
            else -> StartupErrorScreen()
        }
    }
}

private suspend fun initializeInBackground(): Result<Unit> =
    try {
        withContext(Dispatchers.IO) {
            initFirebase()
            val koinApplication = initKoin()

            // Open Firebase and SQLite off the UI thread so the first real screen
            // does not pay their class loading and native-driver startup cost.
            koinApplication.koin.get<ClipboardRepository>()
        }
        Result.success(Unit)
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        Result.failure(exception)
    }

@Composable
private fun StartupScreen() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun StartupErrorScreen() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Failed to start Shared Clipboard")
        }
    }
}
