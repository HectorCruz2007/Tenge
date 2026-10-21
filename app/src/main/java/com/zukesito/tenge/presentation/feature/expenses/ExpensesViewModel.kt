package com.zukesito.tenge.presentation.feature.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.inMonth
import com.zukesito.tenge.core.finance.projectToMonthEnd
import com.zukesito.tenge.core.finance.totalCents
import com.zukesito.tenge.core.finance.totalsByCategory
import com.zukesito.tenge.data.repository.BalanceRepository
import com.zukesito.tenge.data.repository.CategoryRepository
import com.zukesito.tenge.data.repository.ExpenseRepository
import com.zukesito.tenge.data.repository.RecurringRepository
import com.zukesito.tenge.data.repository.SavingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class ExpensesViewModel(
    balanceRepository: BalanceRepository,
    private val expenseRepository: ExpenseRepository,
    categoryRepository: CategoryRepository,
    recurringRepository: RecurringRepository,
    savingsRepository: SavingsRepository,
    today: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState(month = YearMonth.from(today)))
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                balanceRepository.balanceCents,
                expenseRepository.expenses,
                categoryRepository.categories,
                recurringRepository.recurrings,
                savingsRepository.plans
            ) { balance, expenses, categories, recurrings, plans ->
                val names = categories.associate { it.id to it.name }
                val monthExpenses = expenses.inMonth(YearMonth.from(today))
                val totals = monthExpenses.totalsByCategory()
                // La categoría con más gasto llena la barra completa
                val max = totals.values.maxOrNull()?.coerceAtLeast(1L) ?: 1L
                _uiState.value.copy(
                    projection = balance.projectToMonthEnd(recurrings, plans, expenses, today),
                    totalCents = monthExpenses.totalCents(),
                    categoryTotals = totals.map { (id, total) ->
                        CategoryTotal(names[id] ?: id, total, total.toFloat() / max)
                    },
                    items = monthExpenses.map {
                        val category = names[it.categoryId] ?: it.categoryId
                        ExpenseItem(it.id, it.note ?: category, category, it.date, it.amountCents)
                    }
                )
            }.collect { computed ->
                _uiState.update {
                    it.copy(
                        projection = computed.projection,
                        totalCents = computed.totalCents,
                        categoryTotals = computed.categoryTotals,
                        items = computed.items
                    )
                }
            }
        }
    }

    fun onRequestDelete(item: ExpenseItem) = _uiState.update { it.copy(pendingDelete = item) }

    fun onDismissDelete() = _uiState.update { it.copy(pendingDelete = null) }

    fun onConfirmDelete() {
        val item = _uiState.value.pendingDelete ?: return
        _uiState.update { it.copy(pendingDelete = null) }
        viewModelScope.launch {
            expenseRepository.delete(item.id)
            _uiState.update { it.copy(message = R.string.message_expense_deleted) }
        }
    }

    fun onMessageShown() = _uiState.update { it.copy(message = null) }
}
