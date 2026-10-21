package com.zukesito.tenge.presentation.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zukesito.tenge.presentation.components.indicators.CountBadge

/** Título de sección, con contador opcional, ej. "Avisos pendientes (2)". */
@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier, count: Int? = null) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
        if (count != null) CountBadge(count)
    }
}
