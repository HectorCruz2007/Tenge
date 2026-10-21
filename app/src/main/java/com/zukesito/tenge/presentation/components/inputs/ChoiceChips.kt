package com.zukesito.tenge.presentation.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.presentation.components.text.ErrorText
import com.zukesito.tenge.presentation.components.text.FieldLabel

/** Grupo de opciones que se acomodan en varias filas (categorías, plantillas). */
@Composable
fun ChoiceChips(
    label: String,
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel(label)
        FlowRow(
            Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEachIndexed { index, option ->
                OptionButton(option, index == selectedIndex, { onSelect(index) })
            }
        }
        if (error != null) ErrorText(error)
    }
}
