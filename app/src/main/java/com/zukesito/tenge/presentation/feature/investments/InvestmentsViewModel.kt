package com.zukesito.tenge.presentation.feature.investments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.periodsBetween
import com.zukesito.tenge.core.finance.plusPeriods
import com.zukesito.tenge.core.finance.toMxDate
import com.zukesito.tenge.core.finance.toMxShortMonthYear
import com.zukesito.tenge.core.finance.totalValueAt
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

class InvestmentsViewModel(
    private val investmentRepository: InvestmentRepository,
    private val today: LocalDate = LocalDate.now()
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvestmentsUiState(today = today))
    val uiState: StateFlow<InvestmentsUiState> = _uiState.asStateFlow()

    // Fondos originales, necesarios para proyectar
    private var funds: List<InvestmentFund> = emptyList()

    // Periodos a proyectar; se conservan al cambiar de fondo
    private var periods = DEFAULT_PERIODS

    init {
        viewModelScope.launch {
            investmentRepository.funds.collect { list ->
                funds = list
                _uiState.update { state ->
                    // Si el fondo expandido ya no existe, se expande el primero
                    val expanded = state.expandedId?.takeIf { id -> list.any { it.id == id } } ?: list.firstOrNull()?.id
                    state.copy(
                        totalCents = list.totalValueAt(today),
                        funds = list.map { it.toItem() },
                        expandedId = expanded,
                        projection = projectionFor(expanded, today.plusPeriodsOf(expanded, periods))
                    )
                }
            }
        }
    }

    // Tocar un fondo lo expande; tocar el expandido lo contrae
    fun onToggle(id: String) = _uiState.update { state ->
        val expanded = if (state.expandedId == id) null else id
        state.copy(expandedId = expanded, projection = projectionFor(expanded, today.plusPeriodsOf(expanded, periods)))
    }

    // Cambio desde el deslizador de periodos
    fun onPeriodsChange(value: Int) {
        periods = value
        _uiState.update { state ->
            state.copy(projection = projectionFor(state.expandedId, today.plusPeriodsOf(state.expandedId, value)))
        }
    }

    // Cambio desde el calendario
    fun onTargetDateChange(date: LocalDate) = _uiState.update { state ->
        val projection = projectionFor(state.expandedId, date)
        periods = projection?.periods ?: periods
        state.copy(projection = projection)
    }

    fun onRequestDelete(item: FundItem) = _uiState.update { it.copy(pendingDelete = item) }

    fun onDismissDelete() = _uiState.update { it.copy(pendingDelete = null) }

    fun onConfirmDelete() {
        val item = _uiState.value.pendingDelete ?: return
        _uiState.update { it.copy(pendingDelete = null) }
        viewModelScope.launch {
            investmentRepository.delete(item.id)
            _uiState.update { it.copy(message = R.string.message_fund_deleted) }
        }
    }

    fun onMessageShown() = _uiState.update { it.copy(message = null) }

    private fun InvestmentFund.toItem() =
        FundItem(id, name, ratePercent, frequency, registeredAt, valueAt(today))

    private fun LocalDate.plusPeriodsOf(fundId: String?, count: Int): LocalDate {
        val fund = funds.find { it.id == fundId } ?: return this
        return plusPeriods(fund.frequency, count.toLong())
    }

    // Valor estimado en la fecha y tabla de los próximos periodos
    private fun projectionFor(fundId: String?, target: LocalDate): FundProjection? {
        val fund = funds.find { it.id == fundId } ?: return null
        val count = fund.frequency.periodsBetween(today, target).toInt().coerceAtLeast(1)
        val points = (1..minOf(TABLE_ROWS, count)).map { i ->
            val date = today.plusPeriods(fund.frequency, i.toLong())
            val label = if (fund.frequency == Frequency.MONTHLY) date.toMxShortMonthYear() else date.toMxDate()
            ProjectionPoint(label, fund.valueAt(date))
        }
        return FundProjection(target, count, fund.valueAt(target), points)
    }

    private companion object {
        const val DEFAULT_PERIODS = 6
        const val TABLE_ROWS = 3
    }
}
