package com.example.sharedclipboard.test

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun Arrangement(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .zIndex(1f)
                .background(Color.Blue)
        ) {
            Text(
                "A",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f).clickable {
                    println("A")
                }
            )
            Text(
                "B",
                textAlign = TextAlign.Center
            )
        }
        Text(
            "C",
            modifier = Modifier.fillMaxWidth()
                .height(50.dp)
                .offset(y = (-25).dp)
                .background(Color.Yellow)
                .clickable {
                    println("C")
                }
                .wrapContentSize(Alignment.BottomCenter),
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ArrangementPreview() {
    MaterialTheme {
        Arrangement(Modifier.padding(top = 30.dp))
    }
}
