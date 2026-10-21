package com.zukesito.tenge.presentation.components.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.presentation.components.indicators.IconBubble
import com.zukesito.tenge.presentation.components.text.MoneyText
import com.zukesito.tenge.presentation.theme.TengeIcons

/** Movimiento de gasto: título, categoría y fecha, y monto negativo. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseRow(
    title: String,
    subtitle: String,
    amountCents: Long,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                // Mantener presionado para eliminar
                .combinedClickable(onClick = {}, onLongClick = onLongClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBubble(TengeIcons.Expenses)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            MoneyText(-amountCents, signed = true)
        }
        if (showDivider) {
            Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.surfaceContainerHighest))
        }
    }
}
