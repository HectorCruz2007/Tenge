package com.zukesito.tenge.presentation.feature.expenses

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxMonthYear
import com.zukesito.tenge.core.finance.toMxShortDate
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.buttons.AddButton
import com.zukesito.tenge.presentation.components.cards.CategoryBreakdownCard
import com.zukesito.tenge.presentation.components.cards.CategoryShare
import com.zukesito.tenge.presentation.components.cards.RiskCard
import com.zukesito.tenge.presentation.components.feedback.ConfirmDeleteDialog
import com.zukesito.tenge.presentation.components.feedback.EmptyState
import com.zukesito.tenge.presentation.components.lists.ExpenseRow
import com.zukesito.tenge.presentation.components.lists.SectionTitle
import com.zukesito.tenge.presentation.components.templates.TabScreenTemplate

/** Gastos del mes: riesgo, total, desglose por categoría y movimientos. */
@Composable
fun ExpensesScreen(
    onAddExpense: () -> Unit,
    showMessage: (Int) -> Unit,
    viewModel: ExpensesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.message) {
        state.message?.let {
            showMessage(it)
            viewModel.onMessageShown()
        }
    }
    ExpensesContent(state, onAddExpense, viewModel::onRequestDelete)
    state.pendingDelete?.let {
        ConfirmDeleteDialog(
            title = stringResource(R.string.delete_expense_title),
            message = stringResource(R.string.delete_expense_message, it.title, it.date.toMxShortDate(), it.amountCents.toMxn()),
            onConfirm = viewModel::onConfirmDelete,
            onDismiss = viewModel::onDismissDelete
        )
    }
}

@Composable
private fun ExpensesContent(
    state: ExpensesUiState,
    onAddExpense: () -> Unit,
    onRequestDelete: (ExpenseItem) -> Unit
) {
    TabScreenTemplate(
        title = stringResource(R.string.expenses_title),
        headerTrailing = state.month.toMxMonthYear(),
        floatingButton = { AddButton(stringResource(R.string.expenses_add), onAddExpense) }
    ) {
        state.projection?.let { projection ->
            item { RiskCard(projection, state.month.atEndOfMonth(), compact = true) }
        }
        item {
            // Convierte el estado de la pantalla al modelo del componente
            CategoryBreakdownCard(
                state.totalCents,
                state.categoryTotals.map { CategoryShare(it.name, it.totalCents, it.fraction) }
            )
        }
        item { SectionTitle(stringResource(R.string.expenses_movements)) }
        if (state.items.isEmpty()) {
            item { EmptyState(stringResource(R.string.expenses_empty)) }
        }
        itemsIndexed(state.items, key = { _, item -> item.id }) { index, item ->
            ExpenseRow(
                title = item.title,
                subtitle = stringResource(R.string.expense_subtitle, item.categoryName, item.date.toMxShortDate()),
                amountCents = item.amountCents,
                onLongClick = { onRequestDelete(item) },
                showDivider = index < state.items.lastIndex
            )
        }
    }
}
