package com.zukesito.tenge.presentation.feature.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.SavingsStatus
import com.zukesito.tenge.core.finance.estimatedCompletionDate
import com.zukesito.tenge.core.finance.nextDueDate
import com.zukesito.tenge.core.finance.plannedEndDate
import com.zukesito.tenge.core.finance.progress
import com.zukesito.tenge.core.finance.quotaCents
import com.zukesito.tenge.core.finance.status
import com.zukesito.tenge.data.repository.SavingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SavingsViewModel(
    private val savingsRepository: SavingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavingsUiState())
    val uiState: StateFlow<SavingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savingsRepository.plans.collect { plans ->
                val items = plans.map { plan ->
                    SavingsPlanItem(
                        id = plan.id,
                        name = plan.name,
                        status = plan.status(),
                        accumulatedCents = plan.accumulatedCents,
                        goalCents = plan.goalCents,
                        progress = plan.progress(),
                        quotaCents = plan.quotaCents(),
                        termUnit = plan.termUnit,
                        nextDueDate = plan.nextDueDate(),
                        estimatedDate = plan.estimatedCompletionDate(),
                        deadline = plan.plannedEndDate()
                    )
                }
                // Planes activos primero, cumplidos al final
                _uiState.update { state ->
                    state.copy(plans = items.sortedBy { it.status == SavingsStatus.COMPLETED })
                }
            }
        }
    }

    fun onRequestDelete(item: SavingsPlanItem) = _uiState.update { it.copy(pendingDelete = item) }

    fun onDismissDelete() = _uiState.update { it.copy(pendingDelete = null) }

    fun onConfirmDelete() {
        val item = _uiState.value.pendingDelete ?: return
        _uiState.update { it.copy(pendingDelete = null) }
        viewModelScope.launch {
            savingsRepository.delete(item.id)
            _uiState.update { it.copy(message = R.string.message_plan_deleted) }
        }
    }

    fun onMessageShown() = _uiState.update { it.copy(message = null) }
}
