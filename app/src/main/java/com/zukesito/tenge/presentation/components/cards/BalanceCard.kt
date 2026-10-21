package com.zukesito.tenge.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.presentation.components.buttons.OutlinedPillButton
import com.zukesito.tenge.presentation.components.buttons.PrimaryPillButton
import com.zukesito.tenge.presentation.components.text.MoneyText

/** Saldo disponible, patrimonio y botones para modificar el saldo */
@Composable
fun BalanceCard(
    balanceCents: Long,
    netWorthCents: Long,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier, spacing = 6.dp) {
        Text(
            stringResource(R.string.balance_available),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        MoneyText(balanceCents, style = MaterialTheme.typography.displaySmall)
        Text(
            stringResource(R.string.balance_net_worth, netWorthCents.toMxn()),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(2.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedPillButton(
                text = stringResource(R.string.balance_decrease),
                onClick = onDecrease,
                modifier = Modifier.weight(1f),
                emphasized = true
            )
            PrimaryPillButton(
                text = stringResource(R.string.balance_increase),
                onClick = onIncrease,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
