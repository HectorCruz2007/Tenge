package com.zukesito.tenge.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Importancia visual de una línea del resumen. */
enum class SummaryEmphasis { Normal, Bold, Large }

/** Línea del resumen: etiqueta a la izquierda y valor a la derecha. */
data class SummaryLine(
    val label: String,
    val value: String,
    val emphasis: SummaryEmphasis = SummaryEmphasis.Normal,
    // Dibuja una línea divisoria antes de esta fila (ej. antes del total)
    val dividerBefore: Boolean = false
)

/** Recuadro de cálculos, ej. "Saldo actual / Ajuste / Nuevo saldo" o la vista previa de un fondo. */
@Composable
fun SummaryPanel(
    lines: List<SummaryLine>,
    modifier: Modifier = Modifier,
    caption: String? = null
) {
    SectionCard(modifier, soft = true) {
        if (caption != null) {
            Text(caption, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        lines.forEach { line ->
            if (line.dividerBefore) {
                Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.secondaryContainer))
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                val labelColor = if (line.emphasis == SummaryEmphasis.Normal) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurface
                val weight = if (line.emphasis == SummaryEmphasis.Normal) FontWeight.Normal else FontWeight.SemiBold
                Text(line.label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = labelColor,
                    fontWeight = if (line.emphasis == SummaryEmphasis.Bold) FontWeight.SemiBold else FontWeight.Normal)
                Text(
                    line.value,
                    style = if (line.emphasis == SummaryEmphasis.Large) MaterialTheme.typography.titleLarge.copy(fontFeatureSettings = "tnum")
                    else MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "tnum"),
                    fontWeight = weight
                )
            }
        }
    }
}
