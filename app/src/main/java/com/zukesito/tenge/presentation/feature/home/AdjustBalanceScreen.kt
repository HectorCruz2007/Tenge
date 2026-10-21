package com.zukesito.tenge.presentation.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.core.finance.toSignedMxn
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.cards.SummaryEmphasis
import com.zukesito.tenge.presentation.components.cards.SummaryLine
import com.zukesito.tenge.presentation.components.cards.SummaryPanel
import com.zukesito.tenge.presentation.components.inputs.LabeledTextField
import com.zukesito.tenge.presentation.components.inputs.MoneyField
import com.zukesito.tenge.presentation.components.inputs.SegmentedSelector
import com.zukesito.tenge.presentation.components.templates.FormScreenTemplate
import com.zukesito.tenge.presentation.feature.common.message

/** Formulario para aumentar o reducir el saldo manualmente. */
@Composable
fun AdjustBalanceScreen(
    onBack: () -> Unit,
    onSaved: (Int) -> Unit,
    viewModel: AdjustBalanceViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved(R.string.message_balance_updated)
    }
    FormScreenTemplate(
        title = stringResource(R.string.adjust_balance_title),
        saveLabel = stringResource(R.string.action_save),
        onClose = onBack,
        onSave = viewModel::onSave,
        saveEnabled = state.saveEnabled
    ) {
        SegmentedSelector(
            options = listOf(stringResource(R.string.balance_increase_option), stringResource(R.string.balance_decrease_option)),
            selectedIndex = if (state.increase) 0 else 1,
            onSelect = { viewModel.onModeChange(it == 0) }
        )
        MoneyField(
            value = state.amountText,
            onValueChange = viewModel::onAmountChange,
            label = stringResource(R.string.field_amount),
            placeholder = stringResource(R.string.placeholder_adjust_amount),
            error = state.amountError?.message(state.currentBalanceCents)
        )
        LabeledTextField(
            value = state.note,
            onValueChange = viewModel::onNoteChange,
            label = stringResource(R.string.field_note_optional),
            placeholder = stringResource(R.string.placeholder_adjust_note)
        )
        SummaryPanel(
            listOf(
                SummaryLine(stringResource(R.string.adjust_current_balance), state.currentBalanceCents.toMxn()),
                SummaryLine(stringResource(R.string.adjust_adjustment), state.adjustmentCents.toSignedMxn()),
                SummaryLine(
                    stringResource(R.string.adjust_new_balance),
                    state.newBalanceCents.toMxn(),
                    emphasis = SummaryEmphasis.Bold,
                    dividerBefore = true
                )
            )
        )
    }
}
