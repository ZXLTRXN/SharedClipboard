package com.example.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF24C1EB),
    onPrimary = Color(0xFF003544),
    primaryContainer = Color(0xFFD1F5FF),
    onPrimaryContainer = Color(0xFF003544),
    secondary = Color(0xFF50E8CC),
    onSecondary = Color(0xFF00382F),
    secondaryContainer = Color(0xFFC9FFF3),
    onSecondaryContainer = Color(0xFF00382F),
    tertiary = Color(0xFF9A4D75),
    background = Color(0xFFF7F7FD),
    onBackground = Color(0xFF1B1B24),
    surface = Color(0xFFFDFBFF),
    onSurface = Color(0xFF1B1B24),
    surfaceVariant = Color(0xFFE7E5EF),
    onSurfaceVariant = Color(0xFF474650),
    outline = Color(0xFF777680),
    outlineVariant = Color(0xFFC8C5D0),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF5EDAFF),
    onPrimary = Color(0xFF003544),
    primaryContainer = Color(0xFF00566D),
    onPrimaryContainer = Color(0xFFB8EEFF),
    secondary = Color(0xFF66F4D7),
    onSecondary = Color(0xFF00382F),
    secondaryContainer = Color(0xFF005044),
    onSecondaryContainer = Color(0xFFA5F8E6),
    tertiary = Color(0xFFFFB0D2),
    background = Color(0xFF111118),
    onBackground = Color(0xFFE5E1EA),
    surface = Color(0xFF191820),
    onSurface = Color(0xFFE5E1EA),
    surfaceVariant = Color(0xFF474650),
    onSurfaceVariant = Color(0xFFC8C5D0),
    outline = Color(0xFF918F9A),
    outlineVariant = Color(0xFF474650),
)

@Composable
fun SharedClipboardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        shapes = MaterialTheme.shapes.copy(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(20.dp),
            large = RoundedCornerShape(28.dp),
            extraLarge = RoundedCornerShape(36.dp),
        ),
        content = content,
    )
}

/** A calm, uniform base so text contrast does not depend on its screen position. */
@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(scheme.background),
        content = content,
    )
}

/** A lightly elevated surface with predictable contrast in both color schemes. */
@Composable
fun Modifier.glassSurface(strong: Boolean = false): Modifier {
    val scheme = MaterialTheme.colorScheme
    val containerColor = if (strong) scheme.surface else scheme.surface.copy(alpha = 0.96f)
    return this
        .background(
            color = containerColor,
            shape = MaterialTheme.shapes.large,
        )
        .border(
            width = 1.dp,
            color = scheme.outlineVariant.copy(alpha = 0.70f),
            shape = MaterialTheme.shapes.large,
        )
}
