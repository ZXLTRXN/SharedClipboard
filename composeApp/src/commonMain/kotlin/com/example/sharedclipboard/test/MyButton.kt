package com.example.sharedclipboard.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MyButton() {
    Text(
        "Button",
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .graphicsLayer {
                translationX = 8.dp.toPx()
                translationY = 8.dp.toPx()
            }
            .clip(RoundedCornerShape(topStart = 16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = {}
            )
            .drawWithCache {
                val brush = Brush.linearGradient(
                    listOf(
                        Color.Red,
                        Color.Blue
                    )
                )
                onDrawWithContent {
                    drawRoundRect(
                        brush = brush,
                        cornerRadius = CornerRadius(
                            16.dp.toPx(),
                            16.dp.toPx()
                        )
                    )
                    drawContent()
                }
            }
            .padding(12.dp)

    )
}

@Composable
@Preview(
    showBackground = true
)
fun MyButtonPreview() {
    MyButton()
}