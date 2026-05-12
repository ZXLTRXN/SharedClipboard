package com.example.sharedclipboard.test

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.share_ic

@Composable
fun DraggableButton(
    modifier: Modifier = Modifier,
) {

    val containerSize = remember { mutableStateOf(IntSize.Zero) }
    val velocityTracker = remember { VelocityTracker() }
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize().onSizeChanged {
        containerSize.value = it
    }) {

        val offsetX = remember {
            Animatable(0f)
        }
        val offsetY = remember {
            Animatable(0f)
        }
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.Center)
                .offset {
                    IntOffset(
                        offsetX.value.toInt(),
                        offsetY.value.toInt()
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            velocityTracker.resetTracking()
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                val nextX = offsetX.value + dragAmount.x
                                val nextY = offsetY.value + dragAmount.y
                                offsetX.snapTo(
                                    nextX
                                )
                                offsetY.snapTo(
                                    nextY
                                )
                            }
                            velocityTracker.addPointerInputChange(change)
                        },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity()
                            scope.launch {
                                offsetX.animateTo(
                                    0f,
                                    initialVelocity = velocity.x
                                )
                            }
                            scope.launch {
                                offsetY.animateTo(
                                    0f,
                                    initialVelocity = velocity.y
                                )
                            }
                        })
                }
        ) {
            Icon(
                vectorResource(Res.drawable.share_ic),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DraggableButtonPreview() {
    DraggableButton()
}