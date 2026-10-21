package com.zukesito.tenge.presentation.feature.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.MonthEndProjection
import com.zukesito.tenge.core.finance.netWorth
import com.zukesito.tenge.core.finance.projectToMonthEnd
import com.zukesito.tenge.data.model.PendingNotice
import com.zukesito.tenge.data.repository.BalanceRepository
import com.zukesito.tenge.data.repository.ExpenseRepository
import com.zukesito.tenge.data.repository.InsufficientBalanceException
import com.zukesito.tenge.data.repository.InvestmentRepository
import com.zukesito.tenge.data.repository.NoticeRepository
import com.zukesito.tenge.data.repository.RecurringRepository
import com.zukesito.tenge.data.repository.SavingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    balanceRepository: BalanceRepository,
    expenseRepository: ExpenseRepository,
    recurringRepository: RecurringRepository,
    savingsRepository: SavingsRepository,
    investmentRepository: InvestmentRepository,
    private val noticeRepository: NoticeRepository,
    today: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(today = today))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Saldo, patrimonio y riesgo se recalculan con cada cambio de datos
        val summary = combine(
            balanceRepository.balanceCents,
            expenseRepository.expenses,
            recurringRepository.recurrings,
            savingsRepository.plans,
            investmentRepository.funds
        ) { balance, expenses, recurrings, plans, funds ->
            Summary(
                balanceCents = balance,
                netWorthCents = balance.netWorth(funds, today),
                projection = balance.projectToMonthEnd(recurrings, plans, expenses, today)
            )
        }
        viewModelScope.launch {
            summary.combine(noticeRepository.pendingNotices(today)) { totals, notices -> totals to notices }
                .collect { (totals, notices) ->
                    _uiState.update {
                        it.copy(
                            balanceCents = totals.balanceCents,
                            netWorthCents = totals.netWorthCents,
                            projection = totals.projection,
                            notices = notices
                        )
                    }
                }
        }
    }

    fun onConfirmNotice(notice: PendingNotice) =
        run(R.string.message_notice_confirmed) { noticeRepository.confirm(notice) }

    fun onIgnoreNotice(notice: PendingNotice) =
        run(R.string.message_notice_ignored) { noticeRepository.ignore(notice) }

    fun onMessageShown() = _uiState.update { it.copy(message = null) }

    // Ejecuta la acción y deja el mensaje de éxito o de saldo insuficiente
    private fun run(@StringRes success: Int, action: suspend () -> Unit) {
        viewModelScope.launch {
            val message = try {
                action()
                success
            } catch (_: InsufficientBalanceException) {
                R.string.error_insufficient_balance
            }
            _uiState.update { it.copy(message = message) }
        }
    }
}

// Resultado intermedio de combinar los datos financieros
private data class Summary(
    val balanceCents: Long,
    val netWorthCents: Long,
    val projection: MonthEndProjection
)
