package com.zukesito.tenge.presentation.components.cards

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.MonthEndProjection
import com.zukesito.tenge.presentation.components.indicators.ProgressTrack
import com.zukesito.tenge.presentation.labels.labelRes
import com.zukesito.tenge.presentation.theme.color
import kotlin.math.roundToInt

/** Vista previa del riesgo de fin de mes, ej. "Riesgo después de este gasto · Bajo · 66%". */
@Composable
fun RiskPreviewPanel(label: String, projection: MonthEndProjection, modifier: Modifier = Modifier) {
    SectionCard(modifier, soft = true) {
        Row(Modifier.fillMaxWidth()) {
            Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
            Text(
                stringResource(
                    R.string.risk_badge_with_margin,
                    stringResource(projection.level.labelRes()),
                    (projection.margin * 100).roundToInt()
                ),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }
        ProgressTrack(projection.margin, color = projection.level.color())
    }
}
