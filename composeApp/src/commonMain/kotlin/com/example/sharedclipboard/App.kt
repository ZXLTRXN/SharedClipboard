package com.example.sharedclipboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    val repo = remember { FirebaseRepository() }
    var text by remember { mutableStateOf("") }
    var input by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()


    MaterialTheme {
        LaunchedEffect(repo) {
            repo.observeMessages().collect {
                text = it
            }
        }
        Scaffold { contentPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(contentPadding)
            ) {
                Text(text)
                TextField(
                    input,
                    onValueChange = { input = it })
                Button(onClick = {
                    scope.launch() {
                        repo.saveMessage(input)
                    }
                }) {
                    Text("Send")
                }
            }
        }
    }
}