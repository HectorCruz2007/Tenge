package com.zukesito.tenge.presentation.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.core.finance.toSignedMxn

/** Monto en MXN con cifras tabulares para que se alineen en columnas. */
@Composable
fun MoneyText(
    amountCents: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleSmall,
    signed: Boolean = false,
    color: Color = Color.Unspecified
) {
    Text(
        text = if (signed) amountCents.toSignedMxn() else amountCents.toMxn(),
        modifier = modifier,
        color = color,
        style = style.copy(fontFeatureSettings = "tnum")
    )
}
