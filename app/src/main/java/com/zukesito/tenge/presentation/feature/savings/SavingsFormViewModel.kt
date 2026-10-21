package com.zukesito.tenge.presentation.feature.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.isPositiveAmount
import com.zukesito.tenge.core.finance.isValidName
import com.zukesito.tenge.core.finance.isValidTerm
import com.zukesito.tenge.core.finance.plannedEndDate
import com.zukesito.tenge.core.finance.quotaCents
import com.zukesito.tenge.core.finance.toCentsOrNull
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.SavingsPlan
import com.zukesito.tenge.data.repository.SavingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

class SavingsFormViewModel(
    private val savingsRepository: SavingsRepository,
    today: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavingsFormUiState(startDate = today))
    val uiState: StateFlow<SavingsFormUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savingsRepository.templates.collect { templates ->
                _uiState.update { it.copy(templates = templates) }
            }
        }
    }

    // Prellena el formulario con una plantilla
    fun onTemplateSelect(index: Int) = edit {
        val template = it.templates[index]
        it.copy(
            selectedTemplate = index,
            name = template.name,
            goalText = BigDecimal.valueOf(template.goalCents, 2).toPlainString(),
            termText = template.termCount.toString(),
            termUnit = template.termUnit
        )
    }

    fun onNameChange(name: String) = edit { it.copy(name = name) }

    fun onGoalChange(text: String) = edit { it.copy(goalText = text) }

    fun onTermChange(text: String) = edit { it.copy(termText = text) }

    fun onTermUnitChange(unit: Frequency) = edit { it.copy(termUnit = unit) }

    fun onSave() {
        edit { it.copy(submitted = true) }
        val state = _uiState.value
        val goal = state.goalText.toCentsOrNull()
        val term = state.termText.toIntOrNull()
        if (!state.saveEnabled || goal == null || term == null) return
        viewModelScope.launch {
            savingsRepository.add(state.name, goal, term, state.termUnit, state.startDate)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    // Aplica el cambio y recalcula errores y la cuota automática
    private fun edit(change: (SavingsFormUiState) -> SavingsFormUiState) =
        _uiState.update { change(it).validated() }
}

private fun SavingsFormUiState.validated(): SavingsFormUiState {
    val goal = goalText.toCentsOrNull()?.takeIf { it.isPositiveAmount() }
    val term = termText.toIntOrNull()?.takeIf { it.isValidTerm() }
    val nameError = if (submitted && !name.isValidName()) R.string.error_name_required else null
    val goalError = if (submitted && goal == null) R.string.error_amount_positive else null
    val termError = if (submitted && term == null) R.string.error_term_positive else null
    // Plan temporal solo para calcular cuota y fecha meta
    val preview = if (goal != null && term != null) {
        val plan = SavingsPlan("", "", goal, term, termUnit, startDate)
        SavingsPreview(plan.quotaCents(), term, plan.plannedEndDate())
    } else null
    return copy(
        preview = preview,
        nameError = nameError,
        goalError = goalError,
        termError = termError,
        saveEnabled = nameError == null && goalError == null && termError == null
    )
}
