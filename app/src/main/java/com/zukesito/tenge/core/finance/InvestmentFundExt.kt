package com.zukesito.tenge.core.finance

import com.zukesito.tenge.data.model.InvestmentFund
import java.time.LocalDate
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Valor del fondo en date con interés compuesto desde su registro
 */
fun InvestmentFund.valueAt(date: LocalDate): Long {
    val periods = frequency.periodsBetween(registeredAt, date).coerceAtLeast(0)
    val factor = (1 + ratePercent / 100).pow(periods.toDouble())
    // roundToLong se satura en Long.MAX_VALUE en vez de desbordarse
    return (amountCents * factor).roundToLong()
}

/** Ganancia acumulada */
fun InvestmentFund.gainAt(date: LocalDate): Long = valueAt(date) - amountCents

/** Valor total de todos los fondos */
fun List<InvestmentFund>.totalValueAt(date: LocalDate): Long = sumOf { it.valueAt(date) }

/** Patrimonio: saldo disponible + valor actual de los fondos */
fun Long.netWorth(funds: List<InvestmentFund>, today: LocalDate): Long =
    this + funds.totalValueAt(today)