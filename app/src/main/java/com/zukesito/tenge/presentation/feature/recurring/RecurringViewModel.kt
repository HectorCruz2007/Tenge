package com.zukesito.tenge.presentation.feature.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.monthlyEstimateCents
import com.zukesito.tenge.core.finance.nextDueDate
import com.zukesito.tenge.data.model.RecurringType
import com.zukesito.tenge.data.repository.RecurringRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecurringViewModel(
    private val recurringRepository: RecurringRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecurringUiState())
    val uiState: StateFlow<RecurringUiState> = _uiState.asStateFlow()

    // Pestaña elegida (ingresos o gastos)
    private val selectedType = MutableStateFlow(RecurringType.EXPENSE)

    init {
        viewModelScope.launch {
            combine(recurringRepository.recurrings, selectedType) { recurrings, type ->
                val filtered = recurrings.filter { it.type == type }.sortedBy { it.nextDueDate() }
                val estimate = filtered.sumOf { it.monthlyEstimateCents() }
                Triple(
                    type,
                    filtered.map { RecurringItem(it.id, it.name, it.frequency, it.nextDueDate(), it.amountCents) },
                    if (type == RecurringType.EXPENSE) -estimate else estimate
                )
            }.collect { (type, items, estimate) ->
                _uiState.update { it.copy(selectedType = type, items = items, monthlyEstimateCents = estimate) }
            }
        }
    }

    fun onTypeChange(type: RecurringType) {
        selectedType.value = type
    }

    fun onRequestDelete(item: RecurringItem) = _uiState.update { it.copy(pendingDelete = item) }

    fun onDismissDelete() = _uiState.update { it.copy(pendingDelete = null) }

    fun onConfirmDelete() {
        val item = _uiState.value.pendingDelete ?: return
        _uiState.update { it.copy(pendingDelete = null) }
        viewModelScope.launch {
            recurringRepository.delete(item.id)
            _uiState.update { it.copy(message = R.string.message_recurring_deleted) }
        }
    }

    fun onMessageShown() = _uiState.update { it.copy(message = null) }
}
