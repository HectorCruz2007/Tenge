package com.zukesito.tenge.presentation.feature.investments

import androidx.annotation.StringRes
import com.zukesito.tenge.data.model.Frequency

/** Estado del formulario para registrar un fondo. */
data class FundFormUiState(
    val name: String = "",
    val amountText: String = "",
    val rateText: String = "",
    val frequency: Frequency = Frequency.MONTHLY,
    // Vista previa con interés compuesto; null si faltan datos válidos
    val previewShortCents: Long? = null,
    val previewLongCents: Long? = null,
    @param:StringRes val nameError: Int? = null,
    @param:StringRes val amountError: Int? = null,
    // El mensaje de la tasa incluye el máximo de la frecuencia
    val rateError: Boolean = false,
    // Se intentó guardar; a partir de aquí se muestran los errores
    val submitted: Boolean = false,
    val saveEnabled: Boolean = true,
    val isSaved: Boolean = false
) {
    companion object {
        // Periodos de la vista previa ("En 6 meses", "En 12 meses")
        const val SHORT_PERIODS = 6
        const val LONG_PERIODS = 12
    }
}
