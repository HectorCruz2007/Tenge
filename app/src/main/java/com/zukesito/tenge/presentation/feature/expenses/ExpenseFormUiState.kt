package com.zukesito.tenge.presentation.feature.expenses

import com.zukesito.tenge.core.finance.MonthEndProjection
import com.zukesito.tenge.data.model.Category
import com.zukesito.tenge.presentation.feature.common.AmountError
import java.time.LocalDate

/** Estado del formulario para registrar un gasto. */
data class ExpenseFormUiState(
    val categories: List<Category> = emptyList(),
    val amountText: String = "",
    val categoryId: String? = null,
    val date: LocalDate,
    val note: String = "",
    val balanceCents: Long = 0L,
    // Riesgo de fin de mes si se guarda este gasto
    val riskPreview: MonthEndProjection? = null,
    val amountError: AmountError? = null,
    val categoryError: Boolean = false,
    // Se intentó guardar; a partir de aquí se muestran todos los errores
    val submitted: Boolean = false,
    val saveEnabled: Boolean = true,
    val isSaved: Boolean = false
)
