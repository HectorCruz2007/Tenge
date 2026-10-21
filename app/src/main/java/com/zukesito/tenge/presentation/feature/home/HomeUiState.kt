package com.zukesito.tenge.presentation.feature.home

import androidx.annotation.StringRes
import com.zukesito.tenge.core.finance.MonthEndProjection
import com.zukesito.tenge.data.model.PendingNotice
import java.time.LocalDate

/** Estado inmutable de la pantalla de inicio. */
data class HomeUiState(
    val today: LocalDate,
    val balanceCents: Long = 0L,
    val netWorthCents: Long = 0L,
    val projection: MonthEndProjection? = null,
    val notices: List<PendingNotice> = emptyList(),
    // Mensaje de éxito o error pendiente de mostrar
    @param:StringRes val message: Int? = null
)
