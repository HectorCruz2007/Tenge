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
import com.zukesito.tenge.presentation.components.lists.CategoryBarRow
import com.zukesito.tenge.presentation.components.text.MoneyText

/** Una fila del desglose: nombre, total y proporción respecto a la mayor. */
data class CategoryShare(val name: String, val totalCents: Long, val fraction: Float)

/** Total gastado en el mes y desglose por categoría */
@Composable
fun CategoryBreakdownCard(
    totalCents: Long,
    shares: List<CategoryShare>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier, spacing = 10.dp) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.expenses_month_total),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            MoneyText(totalCents, style = MaterialTheme.typography.headlineMedium)
        }
        shares.forEach { CategoryBarRow(it.name, it.totalCents, it.fraction) }
    }
}
