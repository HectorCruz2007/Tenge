package com.zukesito.tenge.presentation.components.feedback

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zukesito.tenge.presentation.components.buttons.PrimaryPillButton

/** Pantalla vacía con ilustración, título, explicación y botón de acción. */
@Composable
fun EmptyPlaceholder(
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        // Ilustración de marcador: rectángulo con una X
        val shape = RoundedCornerShape(12.dp)
        val lineColor = colors.surfaceContainerHighest
        Canvas(
            Modifier
                .size(width = 160.dp, height = 120.dp)
                .clip(shape)
                .background(colors.surfaceContainerHigh)
                .border(1.5.dp, colors.outline, shape)
        ) {
            drawLine(lineColor, Offset.Zero, Offset(size.width, size.height), strokeWidth = 1.5.dp.toPx())
            drawLine(lineColor, Offset(size.width, 0f), Offset(0f, size.height), strokeWidth = 1.5.dp.toPx())
        }
        Text(title, style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp), textAlign = TextAlign.Center)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant, textAlign = TextAlign.Center)
        PrimaryPillButton(actionLabel, onAction, height = 48.dp)
    }
}
