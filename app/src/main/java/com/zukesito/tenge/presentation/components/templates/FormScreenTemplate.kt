package com.zukesito.tenge.presentation.components.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.presentation.components.buttons.PrimaryPillButton
import com.zukesito.tenge.presentation.components.navigation.ScreenHeader

/**
 * Plantilla de formularios: encabezado con ✕, campos desplazables
 * y botón de guardar fijo abajo, deshabilitado mientras haya errores.
 */
@Composable
fun FormScreenTemplate(
    title: String,
    saveLabel: String,
    onClose: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    saveEnabled: Boolean = true,
    formError: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier.fillMaxSize().imePadding()) {
        ScreenHeader(title, onClose = onClose)
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            content = content
        )
        Column(
            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (formError != null) {
                Text(formError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            PrimaryPillButton(saveLabel, onSave, Modifier.fillMaxWidth(), enabled = saveEnabled, height = 52.dp)
        }
    }
}
