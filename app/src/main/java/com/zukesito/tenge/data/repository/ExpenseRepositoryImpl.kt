package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

class ExpenseRepositoryImpl(
    private val dataSource: AppStateDataSource
) : ExpenseRepository {

    override val expenses: Flow<List<Expense>> =
        dataSource.state.map { state -> state.expenses.sortedByDescending { it.date } }
            .distinctUntilChanged()

    override suspend fun add(amountCents: Long, categoryId: String, date: LocalDate, note: String?) {
        val expense = Expense(
            id = UUID.randomUUID().toString(),
            amountCents = amountCents,
            categoryId = categoryId,
            date = date,
            // Nota vacía se guarda como null
            note = note?.trim()?.takeIf { it.isNotEmpty() }
        )
        // Descuento y registro en la misma transacción
        dataSource.update { state ->
            state.withBalanceChange(-amountCents).copy(expenses = state.expenses + expense)
        }
    }

    override suspend fun delete(id: String) {
        dataSource.update { state ->
            val expense = state.expenses.find { it.id == id } ?: return@update state
            state.withBalanceChange(expense.amountCents)
                .copy(expenses = state.expenses - expense)
        }
    }
}