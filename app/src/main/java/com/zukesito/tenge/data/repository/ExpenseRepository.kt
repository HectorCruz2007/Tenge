package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Acceso a los gastos registrados. */
interface ExpenseRepository {
    // Gastos del más reciente al más antiguo
    val expenses: Flow<List<Expense>>

    // Registra un gasto y lo descuenta del saldo, lanza InsufficientBalanceException si el saldo no alcanza
    suspend fun add(amountCents: Long, categoryId: String, date: LocalDate, note: String?)

    /** Elimina un gasto y devuelve su monto al saldo. */
    suspend fun delete(id: String)
}