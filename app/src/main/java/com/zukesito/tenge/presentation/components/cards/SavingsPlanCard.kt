package com.zukesito.tenge.presentation.components.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.presentation.components.indicators.BadgeStyle
import com.zukesito.tenge.presentation.components.indicators.ProgressTrack
import com.zukesito.tenge.presentation.components.indicators.StatusBadge
import com.zukesito.tenge.presentation.components.lists.LabeledValue

/** Datos del detalle de un plan, null en planes cumplidos. */
data class SavingsPlanDetails(
    val quotaLabel: String,
    val nextDueLabel: String,
    val estimatedLabel: String,
    val deadlineLabel: String
)

/** Estado de un plan de ahorro: avance, cuota y fechas. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavingsPlanCard(
    name: String,
    statusLabel: String,
    statusStyle: BadgeStyle,
    accumulatedCents: Long,
    goalCents: Long,
    progress: Float,
    details: SavingsPlanDetails?,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mantener presionado para eliminar
    val clickable = modifier.combinedClickable(onClick = {}, onLongClick = onLongClick)
    SectionCard(clickable, soft = details == null, spacing = 10.dp) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            StatusBadge(statusLabel, style = statusStyle)
        }
        val amounts = stringResource(R.string.savings_progress_amounts, accumulatedCents.toMxn(), goalCents.toMxn())
        if (details == null) {
            // Plan cumplido: versión resumida
            ProgressTrack(progress)
            Text(amounts, style = MaterialTheme.typography.bodySmall)
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(amounts, style = MaterialTheme.typography.bodySmall)
                Text(
                    stringResource(R.string.percent_value, (progress * 100).toInt()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ProgressTrack(progress)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledValue(stringResource(R.string.savings_quota), details.quotaLabel, Modifier.weight(1f))
                LabeledValue(stringResource(R.string.savings_next_due), details.nextDueLabel, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledValue(stringResource(R.string.savings_estimated), details.estimatedLabel, Modifier.weight(1f))
                LabeledValue(stringResource(R.string.savings_deadline), details.deadlineLabel, Modifier.weight(1f))
            }
        }
    }
}
