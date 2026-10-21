package com.zukesito.tenge.presentation.feature.investments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.isPositiveAmount
import com.zukesito.tenge.core.finance.isValidName
import com.zukesito.tenge.core.finance.isValidRateFor
import com.zukesito.tenge.core.finance.plusPeriods
import com.zukesito.tenge.core.finance.toCentsOrNull
import com.zukesito.tenge.core.finance.valueAt
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.InvestmentFund
import com.zukesito.tenge.data.repository.InvestmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class FundFormViewModel(
    private val investmentRepository: InvestmentRepository,
    private val today: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FundFormUiState())
    val uiState: StateFlow<FundFormUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) = edit { it.copy(name = name) }

    fun onAmountChange(text: String) = edit { it.copy(amountText = text) }

    fun onRateChange(text: String) = edit {
        it.copy(rateText = text.filter { c -> c.isDigit() || c == '.' || c == ',' })
    }

    fun onFrequencyChange(frequency: Frequency) = edit { it.copy(frequency = frequency) }

    fun onSave() {
        edit { it.copy(submitted = true) }
        val state = _uiState.value
        val cents = state.amountText.toCentsOrNull()
        val rate = state.rateText.parseRate()
        if (!state.saveEnabled || cents == null || rate == null) return
        viewModelScope.launch {
            // El interés compuesto se calcula desde hoy
            investmentRepository.add(state.name, cents, rate, state.frequency, today)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    // Aplica el cambio y recalcula errores y vista previa
    private fun edit(change: (FundFormUiState) -> FundFormUiState) =
        _uiState.update { validated(change(it)) }

    private fun validated(state: FundFormUiState): FundFormUiState {
        val cents = state.amountText.toCentsOrNull()?.takeIf { it.isPositiveAmount() }
        val rate = state.rateText.parseRate()?.takeIf { it.isValidRateFor(state.frequency) }
        val nameError = if (state.submitted && !state.name.isValidName()) R.string.error_name_required else null
        val amountError = if (state.submitted && cents == null) R.string.error_amount_positive else null
        // La tasa fuera de rango se avisa en cuanto se escribe
        val rateError = (state.rateText.isNotEmpty() || state.submitted) && rate == null
        // Fondo temporal solo para la vista previa
        val fund = if (cents != null && rate != null) {
            InvestmentFund("", "", cents, rate, state.frequency, today)
        } else null
        return state.copy(
            previewShortCents = fund?.valueAt(today.plusPeriods(state.frequency, FundFormUiState.SHORT_PERIODS.toLong())),
            previewLongCents = fund?.valueAt(today.plusPeriods(state.frequency, FundFormUiState.LONG_PERIODS.toLong())),
            nameError = nameError,
            amountError = amountError,
            rateError = rateError,
            saveEnabled = nameError == null && amountError == null && !rateError
        )
    }
}

// Acepta coma o punto decimal
private fun String.parseRate(): Double? = replace(',', '.').toDoubleOrNull()
