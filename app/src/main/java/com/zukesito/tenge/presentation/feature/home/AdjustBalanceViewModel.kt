package com.zukesito.tenge.presentation.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.zukesito.tenge.core.finance.canWithdraw
import com.zukesito.tenge.core.finance.isPositiveAmount
import com.zukesito.tenge.core.finance.toCentsOrNull
import com.zukesito.tenge.data.repository.BalanceRepository
import com.zukesito.tenge.data.repository.InsufficientBalanceException
import com.zukesito.tenge.presentation.feature.common.AmountError
import com.zukesito.tenge.presentation.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class AdjustBalanceViewModel(
    savedStateHandle: SavedStateHandle,
    private val balanceRepository: BalanceRepository,
    private val today: LocalDate = LocalDate.now()
) : ViewModel() {

    // Modo inicial según el botón tocado en inicio
    private val _uiState = MutableStateFlow(
        AdjustBalanceUiState(increase = savedStateHandle.toRoute<Destination.AdjustBalance>().increase)
    )
    val uiState: StateFlow<AdjustBalanceUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            balanceRepository.balanceCents.collect { balance -> edit { it.copy(currentBalanceCents = balance) } }
        }
    }

    fun onModeChange(increase: Boolean) = edit { it.copy(increase = increase) }

    fun onAmountChange(text: String) = edit { it.copy(amountText = text) }

    fun onNoteChange(note: String) = _uiState.update { it.copy(note = note) }

    fun onSave() {
        edit { it.copy(submitted = true) }
        val state = _uiState.value
        val cents = state.amountText.toCentsOrNull()
        if (state.amountError != null || cents == null) return
        viewModelScope.launch {
            try {
                balanceRepository.adjust(if (state.increase) cents else -cents, state.note, today)
                _uiState.update { it.copy(isSaved = true) }
            } catch (_: InsufficientBalanceException) {
                // El saldo cambió mientras se capturaba
                _uiState.update { it.copy(amountError = AmountError.EXCEEDS_BALANCE, saveEnabled = false) }
            }
        }
    }

    // Aplica el cambio y recalcula resumen y errores
    private fun edit(change: (AdjustBalanceUiState) -> AdjustBalanceUiState) =
        _uiState.update { change(it).validated() }
}

private fun AdjustBalanceUiState.validated(): AdjustBalanceUiState {
    val cents = amountText.toCentsOrNull()?.takeIf { it.isPositiveAmount() }
    val delta = when {
        cents == null -> 0L
        increase -> cents
        else -> -cents
    }
    val error = when {
        // Saldo insuficiente se avisa en cuanto se escribe
        cents != null && !currentBalanceCents.canWithdraw(-delta) -> AmountError.EXCEEDS_BALANCE
        submitted && cents == null -> AmountError.NOT_POSITIVE
        else -> null
    }
    return copy(
        adjustmentCents = delta,
        newBalanceCents = currentBalanceCents + delta,
        amountError = error,
        saveEnabled = error == null
    )
}
