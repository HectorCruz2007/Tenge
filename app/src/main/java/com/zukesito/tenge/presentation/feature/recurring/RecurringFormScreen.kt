package com.zukesito.tenge.presentation.feature.recurring

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxWeekdayName
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.RecurringType
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.cards.InfoPanel
import com.zukesito.tenge.presentation.components.inputs.DateField
import com.zukesito.tenge.presentation.components.inputs.LabeledTextField
import com.zukesito.tenge.presentation.components.inputs.MoneyField
import com.zukesito.tenge.presentation.components.inputs.OptionRow
import com.zukesito.tenge.presentation.components.inputs.SegmentedSelector
import com.zukesito.tenge.presentation.components.templates.FormScreenTemplate
import com.zukesito.tenge.presentation.labels.feminineLabelRes

/** Formulario para crear un ingreso o gasto recurrente. */
@Composable
fun RecurringFormScreen(
    onBack: () -> Unit,
    onSaved: (Int) -> Unit,
    viewModel: RecurringFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved(R.string.message_recurring_saved)
    }
    val frequencies = Frequency.entries
    FormScreenTemplate(
        title = stringResource(R.string.recurring_form_title),
        saveLabel = stringResource(R.string.recurring_form_save),
        onClose = onBack,
        onSave = viewModel::onSave,
        saveEnabled = state.saveEnabled
    ) {
        SegmentedSelector(
            options = listOf(stringResource(R.string.recurring_type_income), stringResource(R.string.recurring_type_expense)),
            selectedIndex = if (state.type == RecurringType.INCOME) 0 else 1,
            onSelect = { viewModel.onTypeChange(if (it == 0) RecurringType.INCOME else RecurringType.EXPENSE) }
        )
        LabeledTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = stringResource(R.string.field_name),
            placeholder = stringResource(R.string.placeholder_recurring_name),
            error = state.nameError?.let { stringResource(it) }
        )
        MoneyField(
            value = state.amountText,
            onValueChange = viewModel::onAmountChange,
            label = stringResource(R.string.field_amount),
            placeholder = stringResource(R.string.placeholder_recurring_amount),
            error = state.amountError?.let { stringResource(it) }
        )
        OptionRow(
            label = stringResource(R.string.field_frequency),
            options = frequencies.map { stringResource(it.feminineLabelRes()) },
            selectedIndex = frequencies.indexOf(state.frequency),
            onSelect = { viewModel.onFrequencyChange(frequencies[it]) }
        )
        DateField(state.startDate, viewModel::onStartDateChange, stringResource(R.string.field_start_date))
        // Explica cuándo llegará el aviso, solo con un monto válido
        state.previewAmountCents?.let { cents ->
            val amount = cents.toMxn()
            InfoPanel(
                title = stringResource(R.string.recurring_how_it_works),
                message = when (state.frequency) {
                    Frequency.DAILY -> stringResource(R.string.recurring_how_daily, amount)
                    Frequency.WEEKLY -> stringResource(R.string.recurring_how_weekly, state.startDate.toMxWeekdayName(), amount)
                    Frequency.MONTHLY -> stringResource(R.string.recurring_how_monthly, state.startDate.dayOfMonth, amount)
                }
            )
        }
    }
}
