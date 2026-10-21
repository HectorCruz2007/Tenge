package com.zukesito.tenge.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Contenedor base de las tarjetas: con borde, o relleno suave si soft. */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    soft: Boolean = false,
    spacing: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)
    val styled = if (soft) modifier.background(colors.surfaceContainerHigh, shape)
    else modifier.border(1.5.dp, colors.outline, shape)
    Column(
        styled
            .fillMaxWidth()
            .clip(shape)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(spacing),
        content = content
    )
}
