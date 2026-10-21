package com.zukesito.tenge.presentation.feature.investments

import androidx.annotation.StringRes
import com.zukesito.tenge.data.model.Frequency
import java.time.LocalDate

/** Fondo con su valor actual calculado. */
data class FundItem(
    val id: String,
    val name: String,
    val ratePercent: Double,
    val frequency: Frequency,
    val registeredAt: LocalDate,
    val currentValueCents: Long
)

/** Valor del fondo al final de un periodo, para la tabla. */
data class ProjectionPoint(
    // "Nov 2026" en fondos mensuales, fecha completa en los demás
    val label: String,
    val valueCents: Long
)

/** Proyección del fondo expandido a una fecha futura. */
data class FundProjection(
    val targetDate: LocalDate,
    val periods: Int,
    val estimatedCents: Long,
    // Próximos periodos para la tabla
    val points: List<ProjectionPoint>
)

/** Estado de la pantalla de inversiones. */
data class InvestmentsUiState(
    val today: LocalDate,
    val totalCents: Long = 0L,
    val funds: List<FundItem> = emptyList(),
    val expandedId: String? = null,
    val projection: FundProjection? = null,
    val pendingDelete: FundItem? = null,
    @param:StringRes val message: Int? = null
)
