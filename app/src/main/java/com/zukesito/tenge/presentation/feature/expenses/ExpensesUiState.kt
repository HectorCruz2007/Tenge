package com.zukesito.tenge.presentation.feature.expenses

import androidx.annotation.StringRes
import com.zukesito.tenge.core.finance.MonthEndProjection
import java.time.LocalDate
import java.time.YearMonth

/** Total de una categoría y su proporción respecto a la mayor. */
data class CategoryTotal(
    val name: String,
    val totalCents: Long,
    // De 0 a 1; la categoría con más gasto vale 1
    val fraction: Float
)

/** Gasto listo para mostrarse en la lista. */
data class ExpenseItem(
    val id: String,
    // Nota del gasto o, si no tiene, el nombre de la categoría
    val title: String,
    val categoryName: String,
    val date: LocalDate,
    val amountCents: Long
)

/** Estado de la pantalla de gastos del mes. */
data class ExpensesUiState(
    val month: YearMonth,
    val projection: MonthEndProjection? = null,
    val totalCents: Long = 0L,
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val items: List<ExpenseItem> = emptyList(),
    // Gasto esperando confirmación para eliminarse
    val pendingDelete: ExpenseItem? = null,
    @param:StringRes val message: Int? = null
)
