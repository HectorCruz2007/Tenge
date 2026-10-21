package com.zukesito.tenge.presentation.components.cards

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
import com.zukesito.tenge.core.finance.MonthEndProjection
import com.zukesito.tenge.core.finance.toMxShortDate
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.presentation.components.indicators.ProgressTrack
import com.zukesito.tenge.presentation.components.indicators.StatusBadge
import com.zukesito.tenge.presentation.labels.labelRes
import com.zukesito.tenge.presentation.theme.color
import java.time.LocalDate
import kotlin.math.roundToInt

/**
 * Indicador de riesgo de fin de mes.
 * La barra se llena y cambia de color según el margen disponible.
 */
@Composable
fun RiskCard(
    projection: MonthEndProjection,
    monthEnd: LocalDate,
    modifier: Modifier = Modifier,
    // Versión compacta: sin la línea de saldo proyectado
    compact: Boolean = false
) {
    val level = stringResource(projection.level.labelRes())
    val percent = (projection.margin * 100).roundToInt()
    SectionCard(modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.risk_title), style = MaterialTheme.typography.titleSmall)
            StatusBadge(
                text = if (compact) stringResource(R.string.risk_badge_with_margin, level, percent) else level,
                accent = projection.level.color()
            )
        }
        ProgressTrack(projection.margin, height = 12.dp, color = projection.level.color())
        if (!compact) {
            Text(
                stringResource(
                    R.string.risk_projection,
                    monthEnd.toMxShortDate(),
                    projection.projectedBalanceCents.toMxn(),
                    percent
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
