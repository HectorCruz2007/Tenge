package com.zukesito.tenge.presentation.components.inputs

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Campo numérico corto sin etiqueta, ej. la cantidad del plazo. */
@Composable
fun NumberBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    width: Dp = 80.dp
) {
    OutlinedTextField(
        value = value,
        // Solo dígitos
        onValueChange = { text -> onValueChange(text.filter(Char::isDigit)) },
        modifier = modifier.width(width),
        placeholder = placeholder?.let { { Text(it) } },
        isError = isError,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.outline),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
