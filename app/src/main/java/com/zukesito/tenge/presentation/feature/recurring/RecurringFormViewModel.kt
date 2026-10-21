package com.zukesito.tenge.presentation.feature.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.isPositiveAmount
import com.zukesito.tenge.core.finance.isValidName
import com.zukesito.tenge.core.finance.toCentsOrNull
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.RecurringType
import com.zukesito.tenge.data.repository.RecurringRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class RecurringFormViewModel(
    private val recurringRepository: RecurringRepository,
    today: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecurringFormUiState(startDate = today))
    val uiState: StateFlow<RecurringFormUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) = edit { it.copy(name = name) }

    fun onAmountChange(text: String) = edit { it.copy(amountText = text) }

    fun onTypeChange(type: RecurringType) = edit { it.copy(type = type) }

    fun onFrequencyChange(frequency: Frequency) = edit { it.copy(frequency = frequency) }

    fun onStartDateChange(date: LocalDate) = edit { it.copy(startDate = date) }

    fun onSave() {
        edit { it.copy(submitted = true) }
        val state = _uiState.value
        val cents = state.previewAmountCents
        if (!state.saveEnabled || cents == null) return
        viewModelScope.launch {
            recurringRepository.add(state.name, cents, state.type, state.frequency, state.startDate)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    // Aplica el cambio y recalcula errores y explicación
    private fun edit(change: (RecurringFormUiState) -> RecurringFormUiState) =
        _uiState.update { change(it).validated() }
}

private fun RecurringFormUiState.validated(): RecurringFormUiState {
    val cents = amountText.toCentsOrNull()?.takeIf { it.isPositiveAmount() }
    val nameError = if (submitted && !name.isValidName()) R.string.error_name_required else null
    val amountError = if (submitted && cents == null) R.string.error_amount_positive else null
    return copy(
        previewAmountCents = cents,
        nameError = nameError,
        amountError = amountError,
        saveEnabled = nameError == null && amountError == null
    )
}
