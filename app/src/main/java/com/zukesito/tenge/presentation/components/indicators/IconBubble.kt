package com.zukesito.tenge.presentation.components.indicators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** Círculo de 36 dp con un ícono; relleno o con borde. */
@Composable
fun IconBubble(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    outlined: Boolean = false
) {
    val colors = MaterialTheme.colorScheme
    val base = modifier.size(36.dp)
    val styled = if (outlined) base.border(1.5.dp, colors.onSurface, CircleShape)
    else base.background(colors.secondaryContainer, CircleShape)
    Box(styled, contentAlignment = Alignment.Center) {
        // Decorativo: el texto de la fila ya describe el elemento
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = colors.onSurface)
    }
}
