package com.zukesito.tenge.presentation.components.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.R
import com.zukesito.tenge.presentation.components.text.MoneyText

/**
 * Fondo de inversión con su valor actual.
 * Al tocarlo se expande y muestra expandedContent (proyección).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FundCard(
    name: String,
    rateLabel: String,
    currentValueCents: Long,
    expanded: Boolean,
    onToggle: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    expandedContent: @Composable ColumnScope.() -> Unit = {}
) {
    val clickable = modifier.combinedClickable(onClick = onToggle, onLongClick = onLongClick)
    SectionCard(clickable, spacing = 12.dp) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(name, style = if (expanded) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium)
                Text(rateLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (expanded) {
                    Text(
                        stringResource(R.string.investments_current_value),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                MoneyText(currentValueCents, style = MaterialTheme.typography.titleMedium)
            }
        }
        if (expanded) {
            Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
            expandedContent()
        }
    }
}
