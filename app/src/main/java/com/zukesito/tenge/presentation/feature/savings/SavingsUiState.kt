package com.zukesito.tenge.presentation.feature.savings

import androidx.annotation.StringRes
import com.zukesito.tenge.core.finance.SavingsStatus
import com.zukesito.tenge.data.model.Frequency
import java.time.LocalDate

/** Plan de ahorro con sus valores ya calculados. */
data class SavingsPlanItem(
    val id: String,
    val name: String,
    val status: SavingsStatus,
    val accumulatedCents: Long,
    val goalCents: Long,
    val progress: Float,
    val quotaCents: Long,
    val termUnit: Frequency,
    // null cuando el plan ya se cumplió
    val nextDueDate: LocalDate?,
    val estimatedDate: LocalDate?,
    val deadline: LocalDate
)

/** Estado de la lista de planes de ahorro. */
data class SavingsUiState(
    val plans: List<SavingsPlanItem> = emptyList(),
    val pendingDelete: SavingsPlanItem? = null,
    @param:StringRes val message: Int? = null
)
