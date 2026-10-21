package com.zukesito.tenge.presentation.feature.recurring

import androidx.annotation.StringRes
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.RecurringType
import java.time.LocalDate

/** Estado del formulario para crear un recurrente. */
data class RecurringFormUiState(
    val name: String = "",
    val amountText: String = "",
    val type: RecurringType = RecurringType.EXPENSE,
    val frequency: Frequency = Frequency.MONTHLY,
    val startDate: LocalDate,
    // Monto válido para la explicación "Así funcionará"; null si aún no hay
    val previewAmountCents: Long? = null,
    @param:StringRes val nameError: Int? = null,
    @param:StringRes val amountError: Int? = null,
    // Se intentó guardar; a partir de aquí se muestran los errores
    val submitted: Boolean = false,
    val saveEnabled: Boolean = true,
    val isSaved: Boolean = false
)
