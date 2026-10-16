package com.zukesito.tenge.core.finance

import java.time.LocalDate

/** Ocurrencia de un recurrente o cuota de ahorro con su monto en centavos */
data class Occurrence(
    val date: LocalDate,
    val amountCents: Long
)