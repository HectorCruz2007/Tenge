package com.zukesito.tenge.presentation.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.core.finance.toMxnWhole
import com.zukesito.tenge.presentation.components.indicators.ProgressTrack

/** Fila del desglose: categoría, barra proporcional y total sin decimales. */
@Composable
fun CategoryBarRow(name: String, totalCents: Long, fraction: Float, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            name,
            modifier = Modifier.width(96.dp),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        ProgressTrack(fraction, Modifier.weight(1f), height = 8.dp)
        Text(
            totalCents.toMxnWhole(),
            modifier = Modifier.width(72.dp),
            style = MaterialTheme.typography.bodySmall.copy(fontFeatureSettings = "tnum"),
            textAlign = TextAlign.End
        )
    }
}
