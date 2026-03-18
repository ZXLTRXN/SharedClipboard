package com.example.core.ui.composables

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun FlashTextWithDetection(
    text: String,
    modifier: Modifier = Modifier,
    onLongPress: () -> Unit = {},
    onDoubleTap: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    val baseColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    val targetPrimaryColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    val targetSecondaryColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)

    val color = remember {
        Animatable(
            baseColor
        )
    }

    Text(
        text = text,
        maxLines = 6,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = color.value,
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    style = Fill
                )
            }
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPress()

                        scope.launch {
                            color.snapTo(targetPrimaryColor)
                            color.animateTo(
                                targetValue = baseColor,
                                animationSpec = tween(durationMillis = 700)
                            )
                        }
                    },
                    onDoubleTap = {
                        onDoubleTap()

                        scope.launch {
                            color.snapTo(targetSecondaryColor)
                            color.animateTo(
                                targetValue = baseColor,
                                animationSpec = tween(durationMillis = 700)
                            )
                        }
                    }
                )
            }
    )
}