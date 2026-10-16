package com.zukesito.tenge.core.finance

import com.zukesito.tenge.data.model.Frequency

// Reglas de validación de formularios y saldo

/** Monto mayor a cero */
fun Long.isPositiveAmount(): Boolean = this > 0

/** Nombre no vacío */
fun String.isValidName(): Boolean = isNotBlank()

/** Plazo mayor a cero */
fun Int.isValidTerm(): Boolean = this > 0

/** Tasa mayor a 0 y sin pasar el máximo de su frecuencia */
fun Double.isValidRateFor(frequency: Frequency): Boolean =
    this > 0.0 && this <= frequency.maxRatePercent()

/** true si al restar amountCents el saldo no queda negativo */
fun Long.canWithdraw(amountCents: Long): Boolean = this - amountCents >= 0