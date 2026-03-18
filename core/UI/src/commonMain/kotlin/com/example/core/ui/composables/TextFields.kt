package com.example.core.ui.composables

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import sharedclipboard.core.ui.generated.resources.Res

@Composable
fun ReloadableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onReload: () -> Unit,
    labelRes: StringResource,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value,
        onValueChange = onValueChange,
        modifier = modifier,
        maxLines = 3,
        label = {
            Text(stringResource(labelRes))
        },
        shape = MaterialTheme.shapes.medium,
//        trailingIcon = { fixme
//            IconButton(
//                onClick = onReload
//            ) {
//                Icon(
//                    vectorResource(Res.drawable.res),
//                    contentDescription = null
//                )
//            }
//        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() } // гарантированно закроет
        ),
        keyboardOptions = keyboardOptions
    )
}