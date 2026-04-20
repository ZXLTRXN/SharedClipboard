package com.example.sharedclipboard.test

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedGauge100(
    current: Float,
    modifier: Modifier = Modifier,
) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val padding = 16.dp
    val startAngle = 150f
    val sweepAngle = 240f

    val textMeasurer = rememberTextMeasurer()

    Box(modifier
        .fillMaxWidth()
        .aspectRatio(1f)) {
        Spacer(
            modifier = Modifier.fillMaxSize()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .padding(padding)
                .drawWithCache {
                    println("drawWithCache scale")
                    val strokeWidthPx = 5.dp.toPx()
                    val width = size.width

                    val tickCount = 10
                    val tickOffsets = (0..tickCount).map { i ->
                        val angleInDegrees = startAngle + (i.toFloat() / tickCount) * sweepAngle
                        val angleInRadians = angleInDegrees * (PI / 180f)

                        val innerRadius = (width / 2) - 20.dp.toPx()
                        val outerRadius = (width / 2) - 40.dp.toPx()

                        Offset(
                            x = innerRadius * cos(angleInRadians).toFloat(),
                            y = innerRadius * sin(angleInRadians).toFloat()
                        ) to Offset(
                            x = outerRadius * cos(angleInRadians).toFloat(),
                            y = outerRadius * sin(angleInRadians).toFloat()
                        )
                    }

                    val labels = listOf(
                        "0",
                        "20",
                        "40",
                        "60",
                        "80",
                        "100"
                    )
                    val labelRadius = (size.width / 2) - 55.dp.toPx()

                    val labelOffset = labels.mapIndexed { index, _ ->
                        val angleInDegrees =
                            startAngle + (index.toFloat() / (labels.size - 1)) * sweepAngle
                        val radians = angleInDegrees * (PI.toFloat() / 180f)

                        val x = labelRadius * cos(radians)
                        val y = labelRadius * sin(radians)
                        Offset(
                            x,
                            y
                        )
                    }

                    onDrawBehind {
                        println("drawBehind scale")
                        drawArc(
                            color = secondaryColor,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(
                                width = strokeWidthPx,
                                cap = StrokeCap.Round
                            ),
                        )

                        for (i in 0..tickCount) {
                            val start = Offset(
                                x = center.x + tickOffsets[i].first.x,
                                y = center.y + tickOffsets[i].first.y
                            )

                            val end = Offset(
                                x = center.x + tickOffsets[i].second.x,
                                y = center.y + tickOffsets[i].second.y
                            )

                            drawLine(
                                color = primaryColor,
                                start = start,
                                end = end,
                                strokeWidth = 2.dp.toPx()
                            )
                        }

                        labels.forEachIndexed { index, label ->
                            val x = center.x + labelOffset[index].x
                            val y = center.y + labelOffset[index].y

                            val textLayoutResult = textMeasurer.measure(
                                text = label,
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    color = primaryColor
                                )
                            )

                            drawText(
                                textLayoutResult = textLayoutResult,
                                topLeft = Offset(
                                    x = x - (textLayoutResult.size.width / 2),
                                    y = y - (textLayoutResult.size.height / 2)
                                )
                            )
                        }
                    }
                }
        )
        Arrow(
            current = current,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            max = 100f,
            modifier = Modifier.fillMaxSize()
                .padding(padding)
        )
    }
}

@Composable
fun Arrow(
    current: Float,
    startAngle: Float,
    sweepAngle: Float,
    max: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val animatedSpeed = animateFloatAsState(
        targetValue = current,
        animationSpec = tween(durationMillis = 1000)
    )

    Spacer(
    modifier = modifier.drawWithCache {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val arrowPath = Path().apply {
            moveTo(
                centerX + 0f,
                centerY + 20f
            )
            lineTo(
                centerX - 15f,
                centerY + 0f
            )
            lineTo(
                centerX + 0f,
                centerY - 250f
            )
            lineTo(
                centerX + 15f,
                centerY + 0f
            )
            close()
        }

        onDrawWithContent {

            val ratio = (animatedSpeed.value / max).coerceIn(
                0f,
                1f
            )
            val currentSweep = ratio * sweepAngle

            rotate(startAngle) { // чтобы красный был с начала дуги, а не с 0 градусов
                drawArc(
                    brush = Brush.sweepGradient(
                        0f to Color.Red,
                        0.5f to Color.Blue,
                        1f to Color.Green,
                        center = center,
                    ),
                    startAngle = 0f,
                    sweepAngle = currentSweep,
                    useCenter = false,
                    style = Stroke(
                        width = 5.dp.toPx(),
                        cap = StrokeCap.Round
                    ),
                )
            }

            val speedRotation = startAngle + 90f + currentSweep // 90 т.к стрелка изначально смотрит
            // вверх, а не на 0 (вправо)

            withTransform( // для нескольких трансформаций
                {
                    rotate(speedRotation)
                }
            ) {
                drawPath(
                    arrowPath,
                    color = color
                )
            }
            drawCircle(
                color = color,
                radius = 6.dp.toPx(),
                center = center
            )
        }
    })
}

@Preview
@Composable
fun SpeedGaugePreview() {
    MaterialTheme {
        SpeedGauge100(47f)
    }
}

@Composable
fun SpeedometerTestScreen() {

    var currentSpeed by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            for (speed in 0..100 step 10) {
                currentSpeed = speed.toFloat()
                delay(20)
            }
            delay(1000)
            for (speed in 100 downTo 0) {
                currentSpeed = speed.toFloat()
                delay(10)
            }
            delay(1000)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SpeedGauge100(
            current = currentSpeed,
            modifier = Modifier
                .widthIn(max = 350.dp)
                .fillMaxWidth()
        )
    }
}