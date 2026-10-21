package com.zukesito.tenge.presentation.feature.investments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxDate
import com.zukesito.tenge.core.finance.toPercentText
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.buttons.AddButton
import com.zukesito.tenge.presentation.components.cards.FundCard
import com.zukesito.tenge.presentation.components.cards.SectionCard
import com.zukesito.tenge.presentation.components.feedback.ConfirmDeleteDialog
import com.zukesito.tenge.presentation.components.feedback.EmptyState
import com.zukesito.tenge.presentation.components.inputs.DateField
import com.zukesito.tenge.presentation.components.inputs.PeriodSlider
import com.zukesito.tenge.presentation.components.lists.ProjectionRow
import com.zukesito.tenge.presentation.components.lists.ProjectionTable
import com.zukesito.tenge.presentation.components.templates.TabScreenTemplate
import com.zukesito.tenge.presentation.components.text.MoneyText
import com.zukesito.tenge.presentation.labels.adjectiveRes
import com.zukesito.tenge.presentation.labels.periodPluralRes
import java.time.LocalDate

/** Fondos de inversión con valor actual y proyección a futuro. */
@Composable
fun InvestmentsScreen(
    onAddFund: () -> Unit,
    showMessage: (Int) -> Unit,
    viewModel: InvestmentsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.message) {
        state.message?.let {
            showMessage(it)
            viewModel.onMessageShown()
        }
    }
    InvestmentsContent(
        state = state,
        onAddFund = onAddFund,
        onToggle = viewModel::onToggle,
        onRequestDelete = viewModel::onRequestDelete,
        onPeriodsChange = viewModel::onPeriodsChange,
        onTargetDateChange = viewModel::onTargetDateChange
    )
    state.pendingDelete?.let {
        ConfirmDeleteDialog(
            title = stringResource(R.string.delete_fund_title),
            message = stringResource(R.string.delete_fund_message, it.name),
            onConfirm = viewModel::onConfirmDelete,
            onDismiss = viewModel::onDismissDelete
        )
    }
}

@Composable
private fun InvestmentsContent(
    state: InvestmentsUiState,
    onAddFund: () -> Unit,
    onToggle: (String) -> Unit,
    onRequestDelete: (FundItem) -> Unit,
    onPeriodsChange: (Int) -> Unit,
    onTargetDateChange: (LocalDate) -> Unit
) {
    TabScreenTemplate(
        title = stringResource(R.string.investments_title),
        floatingButton = { AddButton(stringResource(R.string.investments_add), onAddFund) }
    ) {
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.investments_total_today),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                MoneyText(state.totalCents, style = MaterialTheme.typography.headlineMedium)
            }
        }
        if (state.funds.isEmpty()) {
            item { EmptyState(stringResource(R.string.investments_empty)) }
        }
        items(state.funds, key = { it.id }) { fund ->
            val expanded = fund.id == state.expandedId
            val rate = fund.ratePercent.toPercentText()
            val frequency = stringResource(fund.frequency.adjectiveRes())
            FundCard(
                name = fund.name,
                rateLabel = if (expanded) {
                    stringResource(R.string.investments_rate_since, rate, frequency, fund.registeredAt.toMxDate())
                } else {
                    stringResource(R.string.investments_rate, rate, frequency)
                },
                currentValueCents = fund.currentValueCents,
                expanded = expanded,
                onToggle = { onToggle(fund.id) },
                onLongClick = { onRequestDelete(fund) }
            ) {
                state.projection?.let { projection ->
                    FundProjectionSection(fund, projection, state.today, onPeriodsChange, onTargetDateChange)
                }
            }
        }
    }
}

// Contenido del fondo expandido: fecha, periodos, valor estimado y tabla
@Composable
private fun FundProjectionSection(
    fund: FundItem,
    projection: FundProjection,
    today: LocalDate,
    onPeriodsChange: (Int) -> Unit,
    onTargetDateChange: (LocalDate) -> Unit
) {
    Text(stringResource(R.string.investments_projection), style = MaterialTheme.typography.labelLarge)
    DateField(
        date = projection.targetDate,
        onDateChange = onTargetDateChange,
        label = stringResource(R.string.investments_project_to),
        minDate = today.plusDays(1)
    )
    PeriodSlider(
        label = stringResource(R.string.investments_periods),
        valueText = pluralStringResource(fund.frequency.periodPluralRes(), projection.periods, projection.periods),
        value = projection.periods,
        onValueChange = onPeriodsChange
    )
    SectionCard(soft = true) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.investments_estimated_value), style = MaterialTheme.typography.bodySmall)
            MoneyText(projection.estimatedCents, style = MaterialTheme.typography.headlineSmall)
        }
    }
    // Convierte el estado de la pantalla al modelo del componente
    ProjectionTable(projection.points.map { ProjectionRow(it.label, it.valueCents) })
}
