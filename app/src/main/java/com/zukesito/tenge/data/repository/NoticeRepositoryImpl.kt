package com.zukesito.tenge.data.repository

import com.zukesito.tenge.core.finance.dueOccurrencesUntil
import com.zukesito.tenge.core.finance.dueQuotasUntil
import com.zukesito.tenge.core.finance.nextDueDate
import com.zukesito.tenge.core.finance.signedAmountCents
import com.zukesito.tenge.data.datasource.AppState
import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.model.NoticeSource
import com.zukesito.tenge.data.model.PendingNotice
import com.zukesito.tenge.data.model.RecurringType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class NoticeRepositoryImpl(
    private val dataSource: AppStateDataSource
) : NoticeRepository {

    override fun pendingNotices(today: LocalDate): Flow<List<PendingNotice>> =
        dataSource.state.map { it.noticesUntil(today) }.distinctUntilChanged()

    override suspend fun confirm(notice: PendingNotice) {
        dataSource.update { it.handle(notice, confirm = true) }
    }

    override suspend fun ignore(notice: PendingNotice) {
        dataSource.update { it.handle(notice, confirm = false) }
    }
}

// Solo se muestra el aviso más antiguo de cada origen, así se atienden en orden
private fun AppState.noticesUntil(today: LocalDate): List<PendingNotice> {
    val fromRecurring = recurrings.mapNotNull { r ->
        r.dueOccurrencesUntil(today).firstOrNull()?.let {
            val source = if (r.type == RecurringType.INCOME) NoticeSource.RECURRING_INCOME
            else NoticeSource.RECURRING_EXPENSE
            PendingNotice(r.id, source, r.name, it.amountCents, it.date)
        }
    }
    val fromSavings = savingsPlans.mapNotNull { p ->
        p.dueQuotasUntil(today).firstOrNull()?.let {
            PendingNotice(p.id, NoticeSource.SAVINGS_QUOTA, p.name, it.amountCents, it.date)
        }
    }
    return (fromRecurring + fromSavings).sortedBy { it.dueDate }
}

/**
 * Atiende un aviso dentro de la transacción.
 * Si el aviso ya no corresponde a la próxima ocurrencia, no cambia nada.
 */
private fun AppState.handle(notice: PendingNotice, confirm: Boolean): AppState =
    when (notice.source) {
        NoticeSource.RECURRING_INCOME, NoticeSource.RECURRING_EXPENSE -> {
            val recurring = recurrings.find { it.id == notice.sourceId }
            if (recurring == null || recurring.nextDueDate() != notice.dueDate) {
                this
            } else {
                val updated = copy(recurrings = recurrings.map {
                    if (it.id == recurring.id) it.copy(lastHandledDate = notice.dueDate) else it
                })
                // Ingreso suma y gasto resta
                if (confirm) updated.withBalanceChange(recurring.signedAmountCents()) else updated
            }
        }

        NoticeSource.SAVINGS_QUOTA -> {
            val plan = savingsPlans.find { it.id == notice.sourceId }
            val quota = plan?.dueQuotasUntil(notice.dueDate)?.firstOrNull()
            if (plan == null || quota == null || quota.date != notice.dueDate) {
                this
            } else {
                // Ignorar avanza la fecha sin sumar, lo que retrasa el plan
                val accumulated = plan.accumulatedCents + if (confirm) quota.amountCents else 0L
                val handled = plan.copy(lastHandledDate = notice.dueDate, accumulatedCents = accumulated)
                val updated = copy(savingsPlans = savingsPlans.map { if (it.id == plan.id) handled else it })
                // La cuota sale del saldo disponible
                if (confirm) updated.withBalanceChange(-quota.amountCents) else updated
            }
        }
    }
