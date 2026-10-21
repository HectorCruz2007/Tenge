package com.zukesito.tenge.presentation.feature.expenses

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.cards.RiskPreviewPanel
import com.zukesito.tenge.presentation.components.inputs.ChoiceChips
import com.zukesito.tenge.presentation.components.inputs.DateField
import com.zukesito.tenge.presentation.components.inputs.LabeledTextField
import com.zukesito.tenge.presentation.components.inputs.MoneyField
import com.zukesito.tenge.presentation.components.templates.FormScreenTemplate
import com.zukesito.tenge.presentation.feature.common.message

/** Formulario para registrar un gasto con vista previa del riesgo. */
@Composable
fun ExpenseFormScreen(
    onBack: () -> Unit,
    onSaved: (Int) -> Unit,
    viewModel: ExpenseFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved(R.string.message_expense_saved)
    }
    FormScreenTemplate(
        title = stringResource(R.string.expense_form_title),
        saveLabel = stringResource(R.string.expense_form_save),
        onClose = onBack,
        onSave = viewModel::onSave,
        saveEnabled = state.saveEnabled
    ) {
        MoneyField(
            value = state.amountText,
            onValueChange = viewModel::onAmountChange,
            label = stringResource(R.string.field_amount),
            placeholder = stringResource(R.string.placeholder_expense_amount),
            error = state.amountError?.message(state.balanceCents)
        )
        ChoiceChips(
            label = stringResource(R.string.field_category),
            options = state.categories.map { it.name },
            selectedIndex = state.categories.indexOfFirst { it.id == state.categoryId }.takeIf { it >= 0 },
            onSelect = { viewModel.onCategoryChange(state.categories[it].id) },
            error = if (state.categoryError) stringResource(R.string.error_category_required) else null
        )
        DateField(state.date, viewModel::onDateChange, stringResource(R.string.field_date))
        LabeledTextField(
            value = state.note,
            onValueChange = viewModel::onNoteChange,
            label = stringResource(R.string.field_note_optional),
            placeholder = stringResource(R.string.placeholder_expense_note)
        )
        state.riskPreview?.let { RiskPreviewPanel(stringResource(R.string.expense_form_risk_preview), it) }
    }
}
