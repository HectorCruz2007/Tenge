package com.zukesito.tenge.presentation.components.inputs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

/** Campo para capturar un monto en pesos. */
@Composable
fun MoneyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    error: String? = null
) {
    LabeledTextField(
        value = value,
        // Solo dígitos, punto y coma
        onValueChange = { text -> onValueChange(text.filter { it.isDigit() || it == '.' || it == ',' }) },
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        error = error,
        keyboardType = KeyboardType.Decimal,
        prefix = "$"
    )
}
