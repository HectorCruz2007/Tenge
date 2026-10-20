package com.zukesito.tenge.data.repository

import com.zukesito.tenge.core.finance.canWithdraw
import com.zukesito.tenge.data.datasource.AppState

/** Se lanza cuando una operación dejaría el saldo negativo */
class InsufficientBalanceException : IllegalStateException()

/** Suma deltaCents al saldo, lanza la excepción dentro de la transacción, así DataStore no guarda nada */
internal fun AppState.withBalanceChange(deltaCents: Long): AppState {
    if (!balanceCents.canWithdraw(-deltaCents)) throw InsufficientBalanceException()
    return copy(balanceCents = balanceCents + deltaCents)
}