package com.zukesito.tenge.presentation.feature.home

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxDate
import com.zukesito.tenge.core.finance.toMxShortDate
import com.zukesito.tenge.data.model.NoticeSource
import com.zukesito.tenge.data.model.PendingNotice
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.cards.BalanceCard
import com.zukesito.tenge.presentation.components.cards.NoticeCard
import com.zukesito.tenge.presentation.components.cards.RiskCard
import com.zukesito.tenge.presentation.components.feedback.EmptyState
import com.zukesito.tenge.presentation.components.lists.SectionTitle
import com.zukesito.tenge.presentation.components.templates.TabScreenTemplate
import com.zukesito.tenge.presentation.labels.labelRes
import com.zukesito.tenge.presentation.theme.TengeIcons
import java.time.LocalDate
import java.time.YearMonth

/** Pantalla de inicio: saldo, riesgo de fin de mes y avisos pendientes. */
@Composable
fun HomeScreen(
    onAdjustBalance: (increase: Boolean) -> Unit,
    showMessage: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.message) {
        state.message?.let {
            showMessage(it)
            viewModel.onMessageShown()
        }
    }
    HomeContent(state, onAdjustBalance, viewModel::onConfirmNotice, viewModel::onIgnoreNotice)
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onAdjustBalance: (Boolean) -> Unit,
    onConfirm: (PendingNotice) -> Unit,
    onIgnore: (PendingNotice) -> Unit
) {
    TabScreenTemplate(
        title = stringResource(R.string.home_title),
        headerTrailing = state.today.toMxDate()
    ) {
        item {
            BalanceCard(
                balanceCents = state.balanceCents,
                netWorthCents = state.netWorthCents,
                onDecrease = { onAdjustBalance(false) },
                onIncrease = { onAdjustBalance(true) }
            )
        }
        state.projection?.let { projection ->
            item { RiskCard(projection, YearMonth.from(state.today).atEndOfMonth()) }
        }
        item {
            SectionTitle(stringResource(R.string.home_notices), count = state.notices.size.takeIf { it > 0 })
        }
        if (state.notices.isEmpty()) {
            item { EmptyState(stringResource(R.string.home_notices_empty)) }
        }
        items(state.notices, key = { "${it.sourceId}-${it.dueDate}" }) { notice ->
            NoticeCard(
                icon = if (notice.source == NoticeSource.SAVINGS_QUOTA) TengeIcons.Savings else TengeIcons.Recurring,
                title = noticeTitle(notice),
                subtitle = stringResource(
                    R.string.notice_subtitle,
                    stringResource(notice.source.labelRes()),
                    dueLabel(notice.dueDate, state.today)
                ),
                // Solo los ingresos suman al saldo
                signedAmountCents = if (notice.source == NoticeSource.RECURRING_INCOME) notice.amountCents
                else -notice.amountCents,
                onConfirm = { onConfirm(notice) },
                onIgnore = { onIgnore(notice) }
            )
        }
    }
}

@Composable
private fun noticeTitle(notice: PendingNotice): String =
    if (notice.source == NoticeSource.SAVINGS_QUOTA) stringResource(R.string.notice_savings_title, notice.name)
    else notice.name

// "vence hoy" o "venció 1 oct"
@Composable
private fun dueLabel(dueDate: LocalDate, today: LocalDate): String =
    if (dueDate == today) stringResource(R.string.notice_due_today)
    else stringResource(R.string.notice_overdue, dueDate.toMxShortDate())
