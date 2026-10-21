package com.zukesito.tenge.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.R
import com.zukesito.tenge.presentation.components.buttons.OutlinedPillButton
import com.zukesito.tenge.presentation.components.buttons.PrimaryPillButton
import com.zukesito.tenge.presentation.components.indicators.IconBubble
import com.zukesito.tenge.presentation.components.text.MoneyText

/** Aviso pendiente con acciones de confirmar o ignorar */
@Composable
fun NoticeCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    signedAmountCents: Long,
    onConfirm: () -> Unit,
    onIgnore: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier, soft = true, spacing = 10.dp) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconBubble(icon, outlined = true)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            MoneyText(signedAmountCents, signed = true)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedPillButton(stringResource(R.string.notice_ignore), onIgnore, Modifier.weight(1f))
            PrimaryPillButton(stringResource(R.string.notice_confirm), onConfirm, Modifier.weight(1f))
        }
    }
}
