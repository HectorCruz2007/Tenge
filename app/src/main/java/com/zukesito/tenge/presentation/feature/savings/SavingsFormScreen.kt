package com.zukesito.tenge.presentation.feature.savings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxDate
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.cards.SummaryEmphasis
import com.zukesito.tenge.presentation.components.cards.SummaryLine
import com.zukesito.tenge.presentation.components.cards.SummaryPanel
import com.zukesito.tenge.presentation.components.inputs.ChoiceChips
import com.zukesito.tenge.presentation.components.inputs.LabeledTextField
import com.zukesito.tenge.presentation.components.inputs.MoneyField
import com.zukesito.tenge.presentation.components.inputs.TermSelector
import com.zukesito.tenge.presentation.components.templates.FormScreenTemplate
import com.zukesito.tenge.presentation.labels.perPeriodRes
import com.zukesito.tenge.presentation.labels.unitRes

/** Formulario para crear un plan de ahorro, opcionalmente desde una plantilla. */
@Composable
fun SavingsFormScreen(
    onBack: () -> Unit,
    onSaved: (Int) -> Unit,
    viewModel: SavingsFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved(R.string.message_plan_saved)
    }
    val units = Frequency.entries
    FormScreenTemplate(
        title = stringResource(R.string.savings_form_title),
        saveLabel = stringResource(R.string.savings_form_save),
        onClose = onBack,
        onSave = viewModel::onSave,
        saveEnabled = state.saveEnabled
    ) {
        if (state.templates.isNotEmpty()) {
            ChoiceChips(
                label = stringResource(R.string.field_template),
                options = state.templates.map { it.name },
                selectedIndex = state.selectedTemplate,
                onSelect = viewModel::onTemplateSelect
            )
        }
        LabeledTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = stringResource(R.string.field_goal_name),
            placeholder = stringResource(R.string.placeholder_goal_name),
            error = state.nameError?.let { stringResource(it) }
        )
        MoneyField(
            value = state.goalText,
            onValueChange = viewModel::onGoalChange,
            label = stringResource(R.string.field_goal_amount),
            placeholder = stringResource(R.string.placeholder_goal_amount),
            error = state.goalError?.let { stringResource(it) }
        )
        TermSelector(
            label = stringResource(R.string.field_term),
            count = state.termText,
            onCountChange = viewModel::onTermChange,
            units = units.map { stringResource(it.unitRes()) },
            selectedUnit = units.indexOf(state.termUnit),
            onUnitSelect = { viewModel.onTermUnitChange(units[it]) },
            placeholder = stringResource(R.string.placeholder_term),
            error = state.termError?.let { stringResource(it) }
        )
        // Cuota, número de cuotas y fecha meta calculadas automáticamente
        state.preview?.let { preview ->
            SummaryPanel(
                caption = stringResource(R.string.savings_form_calculated),
                lines = listOf(
                    SummaryLine(
                        stringResource(R.string.savings_quota),
                        stringResource(state.termUnit.perPeriodRes(), preview.quotaCents.toMxn()),
                        emphasis = SummaryEmphasis.Large
                    ),
                    SummaryLine(stringResource(R.string.savings_form_installments), preview.installments.toString()),
                    SummaryLine(stringResource(R.string.savings_form_goal_date), preview.goalDate.toMxDate())
                )
            )
        }
    }
}
