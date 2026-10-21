package com.zukesito.tenge.presentation.feature.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxn

/** Error del campo monto; el ViewModel lo decide y la pantalla lo traduce a texto. */
enum class AmountError { NOT_POSITIVE, EXCEEDS_BALANCE }

/** Texto del error; el de saldo insuficiente muestra el saldo disponible. */
@Composable
fun AmountError.message(availableCents: Long): String = when (this) {
    AmountError.NOT_POSITIVE -> stringResource(R.string.error_amount_positive)
    AmountError.EXCEEDS_BALANCE -> stringResource(R.string.error_amount_exceeds_balance, availableCents.toMxn())
}
