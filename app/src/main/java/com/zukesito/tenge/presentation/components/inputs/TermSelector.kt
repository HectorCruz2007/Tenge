package com.zukesito.tenge.presentation.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zukesito.tenge.presentation.components.text.ErrorText
import com.zukesito.tenge.presentation.components.text.FieldLabel

/** Plazo en una línea: cantidad y unidad ("10 | Días · Semanas · Meses"). */
@Composable
fun TermSelector(
    label: String,
    count: String,
    onCountChange: (String) -> Unit,
    units: List<String>,
    selectedUnit: Int,
    onUnitSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    error: String? = null
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel(label)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberBox(count, onCountChange, placeholder = placeholder, isError = error != null)
            Row(Modifier.weight(1f).selectableGroup(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                units.forEachIndexed { index, unit ->
                    OptionButton(
                        unit, index == selectedUnit, { onUnitSelect(index) },
                        Modifier.weight(1f), height = 44.dp, showCheck = false
                    )
                }
            }
        }
        if (error != null) ErrorText(error)
    }
}
