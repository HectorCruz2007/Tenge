package com.zukesito.tenge.core.finance

import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.Recurring
import com.zukesito.tenge.data.model.RecurringType
import java.time.LocalDate

/** Próxima fecha de vencimiento */
fun Recurring.nextDueDate(): LocalDate = frequency.nextDueDate(startDate, lastHandledDate)

/**
 * Ocurrencias no atendidas
 */
fun Recurring.dueOccurrencesUntil(until: LocalDate): List<Occurrence> =
    frequency.occurrencesBetween(startDate, nextDueDate(), until)
        .map { Occurrence(it, amountCents) }

/** Monto con signo: positivo si es ingreso, negativo si es gasto */
fun Recurring.signedAmountCents(): Long =
    if (type == RecurringType.INCOME) amountCents else -amountCents

/** Estimado mensual: diario × 30, semanal × 52/12, mensual × 1. */
fun Recurring.monthlyEstimateCents(): Long = when (frequency) {
    Frequency.DAILY -> amountCents * 30
    Frequency.WEEKLY -> amountCents * 52 / 12
    Frequency.MONTHLY -> amountCents
}
