package com.zukesito.tenge.core.finance

import com.zukesito.tenge.data.model.Expense
import java.time.YearMonth

/** Gastos de un mes específico */
fun List<Expense>.inMonth(month: YearMonth): List<Expense> =
    filter { YearMonth.from(it.date) == month }

/** Total gastado en centavos */
fun List<Expense>.totalCents(): Long = sumOf { it.amountCents }

/** Total por categoría, de mayor a menor */
fun List<Expense>.totalsByCategory(): Map<String, Long> =
    groupBy { it.categoryId }
        .mapValues { (_, expenses) -> expenses.totalCents() }
        .entries
        .sortedByDescending { it.value }
        .associate { it.key to it.value }