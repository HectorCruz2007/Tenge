package com.zukesito.tenge.presentation.feature.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.core.finance.isPositiveAmount
import com.zukesito.tenge.core.finance.projectToMonthEnd
import com.zukesito.tenge.core.finance.toCentsOrNull
import com.zukesito.tenge.data.model.Expense
import com.zukesito.tenge.data.model.Recurring
import com.zukesito.tenge.data.model.SavingsPlan
import com.zukesito.tenge.data.repository.BalanceRepository
import com.zukesito.tenge.data.repository.CategoryRepository
import com.zukesito.tenge.data.repository.ExpenseRepository
import com.zukesito.tenge.data.repository.InsufficientBalanceException
import com.zukesito.tenge.data.repository.RecurringRepository
import com.zukesito.tenge.data.repository.SavingsRepository
import com.zukesito.tenge.presentation.feature.common.AmountError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExpenseFormViewModel(
    private val expenseRepository: ExpenseRepository,
    categoryRepository: CategoryRepository,
    balanceRepository: BalanceRepository,
    recurringRepository: RecurringRepository,
    savingsRepository: SavingsRepository,
    private val today: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseFormUiState(date = today))
    val uiState: StateFlow<ExpenseFormUiState> = _uiState.asStateFlow()

    // Datos necesarios para la vista previa del riesgo
    private var expenses: List<Expense> = emptyList()
    private var recurrings: List<Recurring> = emptyList()
    private var plans: List<SavingsPlan> = emptyList()

    init {
        viewModelScope.launch {
            combine(
                categoryRepository.categories,
                balanceRepository.balanceCents,
                expenseRepository.expenses,
                recurringRepository.recurrings,
                savingsRepository.plans
            ) { categories, balance, expenseList, recurringList, planList ->
                expenses = expenseList
                recurrings = recurringList
                plans = planList
                categories to balance
            }.collect { (categories, balance) ->
                edit { it.copy(categories = categories, balanceCents = balance) }
            }
        }
    }

    fun onAmountChange(text: String) = edit { it.copy(amountText = text) }

    fun onCategoryChange(id: String) = edit { it.copy(categoryId = id) }

    fun onDateChange(date: LocalDate) = edit { it.copy(date = date) }

    fun onNoteChange(note: String) = _uiState.update { it.copy(note = note) }

    fun onSave() {
        edit { it.copy(submitted = true) }
        val state = _uiState.value
        val cents = state.amountText.toCentsOrNull()
        val categoryId = state.categoryId
        if (!state.saveEnabled || cents == null || categoryId == null) return
        viewModelScope.launch {
            try {
                expenseRepository.add(cents, categoryId, state.date, state.note)
                _uiState.update { it.copy(isSaved = true) }
            } catch (_: InsufficientBalanceException) {
                // El saldo cambió mientras se capturaba
                _uiState.update { it.copy(amountError = AmountError.EXCEEDS_BALANCE, saveEnabled = false) }
            }
        }
    }

    // Aplica el cambio y recalcula errores y vista previa
    private fun edit(change: (ExpenseFormUiState) -> ExpenseFormUiState) =
        _uiState.update { validated(change(it)) }

    private fun validated(state: ExpenseFormUiState): ExpenseFormUiState {
        val cents = state.amountText.toCentsOrNull()?.takeIf { it.isPositiveAmount() }
        val amountError = when {
            // Saldo insuficiente se avisa en cuanto se escribe
            cents != null && cents > state.balanceCents -> AmountError.EXCEEDS_BALANCE
            state.submitted && cents == null -> AmountError.NOT_POSITIVE
            else -> null
        }
        val categoryError = state.submitted && state.categoryId == null
        // La vista previa solo considera montos válidos
        val spend = if (cents != null && amountError == null) cents else 0L
        val withExpense = if (spend > 0) expenses + Expense("preview", spend, state.categoryId.orEmpty(), state.date) else expenses
        return state.copy(
            amountError = amountError,
            categoryError = categoryError,
            saveEnabled = amountError == null && !categoryError,
            riskPreview = (state.balanceCents - spend).projectToMonthEnd(recurrings, plans, withExpense, today)
        )
    }
}
