package com.zukesito.tenge.presentation.feature.recurring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxShortDate
import com.zukesito.tenge.data.model.RecurringType
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.buttons.AddButton
import com.zukesito.tenge.presentation.components.cards.RecurringCard
import com.zukesito.tenge.presentation.components.feedback.ConfirmDeleteDialog
import com.zukesito.tenge.presentation.components.feedback.EmptyState
import com.zukesito.tenge.presentation.components.inputs.SegmentedSelector
import com.zukesito.tenge.presentation.components.templates.TabScreenTemplate
import com.zukesito.tenge.presentation.components.text.MoneyText
import com.zukesito.tenge.presentation.labels.labelRes

/** Ingresos y gastos recurrentes con su próximo vencimiento. */
@Composable
fun RecurringScreen(
    onAddRecurring: () -> Unit,
    showMessage: (Int) -> Unit,
    viewModel: RecurringViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.message) {
        state.message?.let {
            showMessage(it)
            viewModel.onMessageShown()
        }
    }
    RecurringContent(state, onAddRecurring, viewModel::onTypeChange, viewModel::onRequestDelete)
    state.pendingDelete?.let {
        ConfirmDeleteDialog(
            title = stringResource(R.string.delete_recurring_title),
            message = stringResource(R.string.delete_recurring_message, it.name),
            onConfirm = viewModel::onConfirmDelete,
            onDismiss = viewModel::onDismissDelete
        )
    }
}

@Composable
private fun RecurringContent(
    state: RecurringUiState,
    onAddRecurring: () -> Unit,
    onTypeChange: (RecurringType) -> Unit,
    onRequestDelete: (RecurringItem) -> Unit
) {
    TabScreenTemplate(
        title = stringResource(R.string.recurring_title),
        floatingButton = { AddButton(stringResource(R.string.recurring_add), onAddRecurring) }
    ) {
        item {
            SegmentedSelector(
                options = listOf(stringResource(R.string.recurring_incomes), stringResource(R.string.recurring_expenses)),
                selectedIndex = if (state.selectedType == RecurringType.INCOME) 0 else 1,
                onSelect = { onTypeChange(if (it == 0) RecurringType.INCOME else RecurringType.EXPENSE) }
            )
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    stringResource(R.string.recurring_monthly_estimate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                MoneyText(state.monthlyEstimateCents, style = MaterialTheme.typography.bodySmall, signed = true)
            }
        }
        if (state.items.isEmpty()) {
            item { EmptyState(stringResource(R.string.recurring_empty)) }
        }
        items(state.items, key = { it.id }) { item ->
            RecurringCard(
                name = item.name,
                frequencyLabel = stringResource(item.frequency.labelRes()),
                nextDueLabel = stringResource(R.string.recurring_next, item.nextDueDate.toMxShortDate()),
                amountCents = item.amountCents,
                onLongClick = { onRequestDelete(item) }
            )
        }
    }
}
