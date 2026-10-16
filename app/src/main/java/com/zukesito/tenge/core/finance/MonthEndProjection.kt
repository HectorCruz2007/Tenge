package com.zukesito.tenge.core.finance

import com.zukesito.tenge.data.model.Expense
import com.zukesito.tenge.data.model.Recurring
import com.zukesito.tenge.data.model.RecurringType
import com.zukesito.tenge.data.model.SavingsPlan
import java.time.LocalDate
import java.time.YearMonth

/** Resultado de proyectar el saldo al último día del mes */
data class MonthEndProjection(
    val projectedBalanceCents: Long,
    // Margen de 0 a 1: llenado de la barra de riesgo
    val margin: Float,
    val level: RiskLevel
)

/**
 * Proyecta el saldo en centavos al fin del mes
 */
fun Long.projectToMonthEnd(
    recurrings: List<Recurring>,
    savingsPlans: List<SavingsPlan>,
    expenses: List<Expense>,
    today: LocalDate
): MonthEndProjection {
    val month = YearMonth.from(today)
    val monthEnd = month.atEndOfMonth()

    // Recurrentes y cuotas que faltan por aplicar este mes
    fun List<Recurring>.pendingTotal(type: RecurringType) =
        filter { it.type == type }.sumOf { r -> r.dueOccurrencesUntil(monthEnd).sumOf { it.amountCents } }
    val incomes = recurrings.pendingTotal(RecurringType.INCOME)
    val fixedExpenses = recurrings.pendingTotal(RecurringType.EXPENSE)
    val savings = savingsPlans.sumOf { p -> p.dueQuotasUntil(monthEnd).sumOf { it.amountCents } }

    // Ritmo de gasto variable: promedio diario del mes × días restantes
    val spent = expenses.inMonth(month).filter { !it.date.isAfter(today) }.totalCents()
    val variable = spent / today.dayOfMonth * (monthEnd.dayOfMonth - today.dayOfMonth)

    val available = this + incomes
    val projected = available - fixedExpenses - savings - variable
    val margin = if (available <= 0) 0f
    else (projected.toDouble() / available).coerceIn(0.0, 1.0).toFloat()

    return MonthEndProjection(projected, margin, riskLevelOf(projected, margin))
}

// Umbrales: 50 % del dinero disponible sobra → bajo; 25 % → medio; menos → alto; negativo → crítico
private fun riskLevelOf(projectedCents: Long, margin: Float): RiskLevel = when {
    projectedCents < 0 -> RiskLevel.CRITICAL
    margin >= 0.5f -> RiskLevel.LOW
    margin >= 0.25f -> RiskLevel.MEDIUM
    else -> RiskLevel.HIGH
}