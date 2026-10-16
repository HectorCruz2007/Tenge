package com.zukesito.tenge.core.finance

import com.zukesito.tenge.data.model.Frequency
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/** Suma X periodos de la frecuencia a la fecha */
fun LocalDate.plusPeriods(frequency: Frequency, count: Long = 1): LocalDate = when (frequency) {
    Frequency.DAILY -> plusDays(count)
    Frequency.WEEKLY -> plusWeeks(count)
    Frequency.MONTHLY -> plusMonths(count)
}

/** Periodos completos transcurridos entre dos fechas */
fun Frequency.periodsBetween(from: LocalDate, to: LocalDate): Long = when (this) {
    Frequency.DAILY -> ChronoUnit.DAYS.between(from, to)
    Frequency.WEEKLY -> ChronoUnit.WEEKS.between(from, to)
    Frequency.MONTHLY -> ChronoUnit.MONTHS.between(from, to)
}

fun Frequency.occurrenceIndex(start: LocalDate, date: LocalDate): Long {
    if (!date.isAfter(start)) return 0
    var n = periodsBetween(start, date)
    while (start.plusPeriods(this, n).isBefore(date)) n++
    return n
}

/** Primera ocurrencia estrictamente posterior a la fecha */
fun Frequency.firstOccurrenceAfter(start: LocalDate, date: LocalDate): LocalDate {
    if (date.isBefore(start)) return start
    var n = occurrenceIndex(start, date)
    var next = start.plusPeriods(this, n)
    while (!next.isAfter(date)) next = start.plusPeriods(this, ++n)
    return next
}

/** Próxima ocurrencia pendiente según la última atendida */
fun Frequency.nextDueDate(start: LocalDate, lastHandled: LocalDate?): LocalDate =
    if (lastHandled == null) start else firstOccurrenceAfter(start, lastHandled)

fun Frequency.occurrencesBetween(start: LocalDate, from: LocalDate, until: LocalDate): List<LocalDate> =
    generateSequence(from) { firstOccurrenceAfter(start, it) }
        .takeWhile { !it.isAfter(until) }
        .toList()

/**
 * Tasa máxima por periodo, equivalente a 100 % anual (duplicar en un año).
 * Sirve para detectar errores de captura como 15 en vez de 1.5.
 */
fun Frequency.maxRatePercent(): Double = when (this) {
    Frequency.DAILY -> 0.19
    Frequency.WEEKLY -> 1.34
    Frequency.MONTHLY -> 5.95
}