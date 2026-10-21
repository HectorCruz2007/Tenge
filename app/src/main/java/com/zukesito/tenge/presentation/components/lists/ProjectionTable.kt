package com.zukesito.tenge.presentation.components.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.R
import com.zukesito.tenge.presentation.components.text.MoneyText

/** Fila de la tabla: etiqueta del periodo y valor del fondo. */
data class ProjectionRow(val label: String, val valueCents: Long)

/** Tabla "Periodo | Valor" de los próximos periodos de un fondo. */
@Composable
fun ProjectionTable(rows: List<ProjectionRow>, modifier: Modifier = Modifier) {
    val style = MaterialTheme.typography.bodySmall
    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(
                stringResource(R.string.investments_table_period),
                Modifier.weight(1f),
                style = style,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.investments_table_value),
                style = style,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        rows.forEach { row ->
            Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.surfaceContainerHighest))
            Row(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                Text(row.label, Modifier.weight(1f), style = style)
                MoneyText(row.valueCents, style = style)
            }
        }
    }
}
