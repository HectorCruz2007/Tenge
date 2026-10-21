package com.zukesito.tenge.presentation.components.indicators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Variantes visuales del badge según su importancia. */
enum class BadgeStyle { Soft, Outlined, Strong }

/** Etiqueta corta de estado, ej. "Bajo", "En curso", "Cumplido". */
@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    style: BadgeStyle = BadgeStyle.Soft,
    // Color opcional para teñir el badge (riesgo)
    accent: Color? = null
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(12.dp)
    val (background, content) = when (style) {
        BadgeStyle.Soft -> (accent?.copy(alpha = 0.18f) ?: colors.surfaceContainerHighest) to colors.onSurface
        BadgeStyle.Outlined -> Color.Transparent to colors.onSurface
        BadgeStyle.Strong -> colors.primary to colors.onPrimary
    }
    val base = modifier.background(background, shape)
    val bordered = if (style == BadgeStyle.Outlined) base.border(1.5.dp, colors.onSurface, shape) else base
    Text(
        text = text,
        modifier = bordered.padding(horizontal = 10.dp, vertical = 3.dp),
        color = content,
        style = MaterialTheme.typography.labelMedium
    )
}
