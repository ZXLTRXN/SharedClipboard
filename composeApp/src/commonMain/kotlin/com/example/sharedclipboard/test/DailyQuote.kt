package com.example.sharedclipboard.test

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

fun Double.roundTo2(): Double {
    return (this * 100).roundToInt() / 100.0
}

@Composable
fun DailyQuote(
    min: Double,
    max: Double,
    open: Double,
    close: Double,
    current: Double,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp),
        contentAlignment = Alignment.Center
    ) {

        val animatedCurrent = animateFloatAsState(current.toFloat())
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .drawWithCache {
                    val lineHorizontalOffset = 24.dp.toPx()
                    val textVerticalOffset = 4.dp.toPx()
                    val lineWidth = size.width - lineHorizontalOffset * 2

                    val currentPath = Path().apply {
                        moveTo(0f, 16f)
                        lineTo(0f, 32f)
                        lineTo(16f, 32f)
                        lineTo(16f, 16f)
                        lineTo(8f, 8f)
                        close()
                    }

                    onDrawWithContent {
                        drawLine(
                            color = Color.Gray,
                            start = Offset(
                                lineHorizontalOffset,
                                center.y
                            ),
                            end = Offset(
                                size.width - lineHorizontalOffset,
                                center.y
                            ),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Butt
                        )

                        drawLabel(
                            value = min,
                            x = { lineHorizontalOffset - it.size.width / 2 },
                            y = { center.y + textVerticalOffset },
                            textMeasurer = textMeasurer
                        )

                        drawLabel(
                            value = max,
                            x = { size.width - lineHorizontalOffset - it.size.width / 2 },
                            y = { center.y + textVerticalOffset },
                            textMeasurer = textMeasurer
                        )

                        val len = max - min
                        val openCoord =
                            lineHorizontalOffset + ((open - min) / len).toFloat() * lineWidth
                        val closeCoord =
                            lineHorizontalOffset + ((close - min) / len).toFloat() * lineWidth
                        val currentCoord =
                            lineHorizontalOffset + ((animatedCurrent.value - min) / len).toFloat() *
                                    lineWidth

                        drawLine(
                            color = Color.Red,
                            start = Offset(
                                openCoord,
                                center.y
                            ),
                            end = Offset(
                                closeCoord,
                                center.y
                            ),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Square
                        )

                        drawLabel(
                            value = open,
                            x = { openCoord - it.size.width / 2 },
                            y = { center.y + textVerticalOffset },
                            textMeasurer = textMeasurer
                        )

                        drawLabel(
                            value = close,
                            x = { closeCoord - it.size.width / 2 },
                            y = { center.y + textVerticalOffset },
                            textMeasurer = textMeasurer
                        )

                        translate(currentCoord - 8.dp.toPx(), center.y - 1.dp.toPx()) {
                            drawPath(currentPath, color = Color.Red)
                        }

                        drawLabel(
                            value = current,
                            x = { currentCoord - it.size.width / 2 },
                            y = { center.y + textVerticalOffset + 32f },
                            textMeasurer = textMeasurer
                        )

                    }
                }
        )
    }


}

inline fun DrawScope.drawLabel(
    value: Double,
    x: (TextLayoutResult) -> Float,
    y: (TextLayoutResult) -> Float,
    textMeasurer: TextMeasurer
) {
    val measureRes = textMeasurer.measure(
        value.roundTo2().toString(),
        style =
            TextStyle(
                color = Color.Black,
                fontSize = 12.sp
            )
    )

    drawText(
        measureRes,
        topLeft = Offset(
            x = x(measureRes),
            y = y(measureRes)
        )
    )
}


@Composable
@Preview(showBackground = true)
fun DailyQuotePreview() {
    DailyQuoteLiveDemo()
}

@Composable
fun DailyQuoteLiveDemo() {

    var currentVal by remember { mutableStateOf(50.0) }
    var isIncreasing by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            if (isIncreasing) {
                currentVal += 4.5
                if (currentVal >= 90.0) isIncreasing = false
            } else {
                currentVal -= 3.5
                if (currentVal <= 20.0) isIncreasing = true
            }
        }
    }

    DailyQuote(
        min = 10.0,
        max = 100.0,
        open = 40.0,
        close = 80.0,
        current = currentVal,
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp, horizontal = 16.dp)
    )
}