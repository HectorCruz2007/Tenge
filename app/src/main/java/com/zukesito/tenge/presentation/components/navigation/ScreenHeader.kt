package com.zukesito.tenge.presentation.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zukesito.tenge.R
import com.zukesito.tenge.presentation.theme.TengeIcons

/**
 * Encabezado de 64 dp con título y texto secundario opcional.
 * Con onClose muestra una ✕ a la izquierda, como en los formularios.
 */
@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    onClose: (() -> Unit)? = null
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = if (onClose == null) 16.dp else 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (onClose != null) {
                IconButton(onClick = onClose) {
                    Icon(TengeIcons.Close, contentDescription = stringResource(R.string.action_close))
                }
            }
            Text(
                title,
                Modifier.weight(1f),
                // Los formularios usan un título un poco más chico
                style = if (onClose == null) MaterialTheme.typography.headlineSmall
                else MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp)
            )
            if (trailingText != null) {
                Text(
                    trailingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
    }
}
