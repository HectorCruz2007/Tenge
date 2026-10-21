package com.zukesito.tenge.presentation.feature.savings

import androidx.annotation.StringRes
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.SavingsTemplate
import java.time.LocalDate

/** Resultado calculado por la app mientras se llena el formulario. */
data class SavingsPreview(
    val quotaCents: Long,
    val installments: Int,
    // Fecha de la última cuota
    val goalDate: LocalDate
)

/** Estado del formulario para crear un plan de ahorro. */
data class SavingsFormUiState(
    val templates: List<SavingsTemplate> = emptyList(),
    val selectedTemplate: Int? = null,
    val name: String = "",
    val goalText: String = "",
    val termText: String = "",
    val termUnit: Frequency = Frequency.MONTHLY,
    val startDate: LocalDate,
    // null si faltan datos válidos
    val preview: SavingsPreview? = null,
    @param:StringRes val nameError: Int? = null,
    @param:StringRes val goalError: Int? = null,
    @param:StringRes val termError: Int? = null,
    // Se intentó guardar; a partir de aquí se muestran los errores
    val submitted: Boolean = false,
    val saveEnabled: Boolean = true,
    val isSaved: Boolean = false
)
