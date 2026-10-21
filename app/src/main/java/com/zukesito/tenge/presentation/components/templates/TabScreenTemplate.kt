package com.zukesito.tenge.presentation.components.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.presentation.components.navigation.ScreenHeader

/**
 * Plantilla de las secciones principales: encabezado, lista desplazable
 * y botón flotante opcional en la esquina inferior derecha.
 */
@Composable
fun TabScreenTemplate(
    title: String,
    modifier: Modifier = Modifier,
    headerTrailing: String? = null,
    floatingButton: (@Composable () -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    Column(modifier.fillMaxSize()) {
        ScreenHeader(title, trailingText = headerTrailing)
        Box(Modifier.weight(1f)) {
            LazyColumn(
                Modifier.fillMaxSize(),
                // Espacio inferior extra para que el botón flotante no tape el último elemento
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
            if (floatingButton != null) {
                Box(Modifier.align(Alignment.BottomEnd).padding(16.dp)) { floatingButton() }
            }
        }
    }
}
