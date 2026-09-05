package com.example.core.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TwoTrailingTextField(
    inputState: MutableState<String>,
    onValueChange: (String) -> Unit,
    labelRes: StringResource,
    firstIcon: DrawableResource,
    secondIcon: DrawableResource,
    onFirstClick: () -> Unit,
    onSecondClick: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
) {
    val focusManager = LocalFocusManager.current

    TextField(
        inputState.value,
        onValueChange = onValueChange,
        modifier = modifier.onPreviewKeyEvent { event ->
            if (
                event.key == Key.Enter &&
                event.type == KeyEventType.KeyDown &&
                !event.isShiftPressed
            ) {
                onSubmit()
                true
            } else {
                false
            }
        },
        maxLines = 3,
        label = {
            Text(stringResource(labelRes))
        },
        shape = MaterialTheme.shapes.medium,
        trailingIcon = {
            Row {
                IconButton(
                    onClick = onFirstClick
                ) {
                    Icon(
                        vectorResource(firstIcon),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = onSecondClick
                ) {
                    Icon(
                        vectorResource(secondIcon),
                        contentDescription = null
                    )
                }
            }

        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onSubmit()
                focusManager.clearFocus()
            }
        ),
        keyboardOptions = keyboardOptions
    )
}
