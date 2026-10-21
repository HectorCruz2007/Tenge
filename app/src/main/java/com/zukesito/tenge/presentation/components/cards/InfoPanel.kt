package com.zukesito.tenge.presentation.components.cards

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Recuadro informativo con título y explicación, ej. "Así funcionará". */
@Composable
fun InfoPanel(title: String, message: String, modifier: Modifier = Modifier) {
    SectionCard(modifier, soft = true, spacing = 4.dp) {
        Text(title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
