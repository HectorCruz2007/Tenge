package com.zukesito.tenge.core.finance

import com.zukesito.tenge.data.model.SavingsPlan
import java.time.LocalDate

/** Cuota por periodo, redondeada hacia arriba para alcanzar la meta */
fun SavingsPlan.quotaCents(): Long =
    if (termCount <= 0) goalCents else (goalCents + termCount - 1) / termCount

/** Lo que falta para la meta */
fun SavingsPlan.remainingCents(): Long = (goalCents - accumulatedCents).coerceAtLeast(0)

/** Cumplido cuando el acumulado alcanza la meta */
fun SavingsPlan.isCompleted(): Boolean = accumulatedCents >= goalCents

/** Cuotas que faltan, la última puede ser menor a la cuota normal */
fun SavingsPlan.remainingQuotas(): Long {
    val quota = quotaCents()
    return if (quota <= 0) 0 else (remainingCents() + quota - 1) / quota
}

/** Avance de 0 a 1 para la barra de progreso */
fun SavingsPlan.progress(): Float =
    if (goalCents <= 0) 1f else (accumulatedCents.toDouble() / goalCents).coerceIn(0.0, 1.0).toFloat()

/** Próxima fecha de aporte, null si el plan ya se cumplió */
fun SavingsPlan.nextDueDate(): LocalDate? =
    if (isCompleted()) null else termUnit.nextDueDate(startDate, lastHandledDate)

/** Fecha de la última cuota según el plazo original */
fun SavingsPlan.plannedEndDate(): LocalDate = startDate.plusPeriods(termUnit, termCount - 1L)

/**
 * Fecha estimada de cumplimiento: próxima cuota + cuotas restantes.
 * Ignorar una cuota mueve la próxima fecha sin sumar acumulado, por eso la retrasa
 */
fun SavingsPlan.estimatedCompletionDate(): LocalDate? {
    val next = nextDueDate() ?: return null
    val nextIndex = termUnit.occurrenceIndex(startDate, next)
    return startDate.plusPeriods(termUnit, nextIndex + remainingQuotas() - 1)
}

/** En curso, atrasado o cumplido. */
fun SavingsPlan.status(): SavingsStatus = when {
    isCompleted() -> SavingsStatus.COMPLETED
    estimatedCompletionDate()?.isAfter(plannedEndDate()) == true -> SavingsStatus.BEHIND
    else -> SavingsStatus.IN_PROGRESS
}

/**
 * Cuotas no atendidas hasta Until inclusive, sin pasarse de la meta.
 * Con Until = hoy se obtienen los avisos pendientes
 */
fun SavingsPlan.dueQuotasUntil(until: LocalDate): List<Occurrence> {
    val next = nextDueDate() ?: return emptyList()
    val quota = quotaCents()
    var left = remainingCents()
    return termUnit.occurrencesBetween(startDate, next, until)
        .take(remainingQuotas().toInt())
        .map { date ->
            val amount = minOf(quota, left)
            left -= amount
            Occurrence(date, amount)
        }
}