package com.zukesito.tenge.presentation.feature.recurring

import androidx.annotation.StringRes
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.RecurringType
import java.time.LocalDate

/** Recurrente listo para mostrarse. */
data class RecurringItem(
    val id: String,
    val name: String,
    val frequency: Frequency,
    val nextDueDate: LocalDate,
    val amountCents: Long
)

/** Estado de la lista de recurrentes, separada en ingresos y gastos. */
data class RecurringUiState(
    val selectedType: RecurringType = RecurringType.EXPENSE,
    val items: List<RecurringItem> = emptyList(),
    // Negativo para gastos, positivo para ingresos
    val monthlyEstimateCents: Long = 0L,
    val pendingDelete: RecurringItem? = null,
    @param:StringRes val message: Int? = null
)
