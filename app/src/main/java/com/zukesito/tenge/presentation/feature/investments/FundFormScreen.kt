package com.zukesito.tenge.presentation.feature.investments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.maxRatePercent
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.core.finance.toPercentText
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.cards.SummaryEmphasis
import com.zukesito.tenge.presentation.components.cards.SummaryLine
import com.zukesito.tenge.presentation.components.cards.SummaryPanel
import com.zukesito.tenge.presentation.components.inputs.LabeledTextField
import com.zukesito.tenge.presentation.components.inputs.MoneyField
import com.zukesito.tenge.presentation.components.inputs.OptionRow
import com.zukesito.tenge.presentation.components.templates.FormScreenTemplate
import com.zukesito.tenge.presentation.labels.adjectiveRes
import com.zukesito.tenge.presentation.labels.labelRes
import com.zukesito.tenge.presentation.labels.periodPluralRes

/** Formulario para registrar un fondo de inversión con vista previa. */
@Composable
fun FundFormScreen(
    onBack: () -> Unit,
    onSaved: (Int) -> Unit,
    viewModel: FundFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved(R.string.message_fund_saved)
    }
    val frequencies = Frequency.entries
    FormScreenTemplate(
        title = stringResource(R.string.fund_form_title),
        saveLabel = stringResource(R.string.fund_form_save),
        onClose = onBack,
        onSave = viewModel::onSave,
        saveEnabled = state.saveEnabled
    ) {
        LabeledTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = stringResource(R.string.field_name),
            placeholder = stringResource(R.string.placeholder_fund_name),
            error = state.nameError?.let { stringResource(it) }
        )
        MoneyField(
            value = state.amountText,
            onValueChange = viewModel::onAmountChange,
            label = stringResource(R.string.field_invested_amount),
            placeholder = stringResource(R.string.placeholder_fund_amount),
            error = state.amountError?.let { stringResource(it) }
        )
        LabeledTextField(
            value = state.rateText,
            onValueChange = viewModel::onRateChange,
            label = stringResource(R.string.field_rate),
            placeholder = stringResource(R.string.placeholder_fund_rate),
            // Muestra el máximo permitido para la frecuencia elegida
            error = if (state.rateError) {
                stringResource(
                    R.string.error_rate_range,
                    state.frequency.maxRatePercent().toPercentText(),
                    stringResource(state.frequency.adjectiveRes())
                )
            } else null,
            keyboardType = KeyboardType.Decimal,
            suffix = "%"
        )
        OptionRow(
            label = stringResource(R.string.field_rate_frequency),
            options = frequencies.map { stringResource(it.labelRes()) },
            selectedIndex = frequencies.indexOf(state.frequency),
            onSelect = { viewModel.onFrequencyChange(frequencies[it]) }
        )
        val short = state.previewShortCents
        val long = state.previewLongCents
        if (short != null && long != null) {
            val unit = state.frequency.periodPluralRes()
            SummaryPanel(
                caption = stringResource(R.string.fund_form_preview),
                lines = listOf(
                    SummaryLine(
                        stringResource(
                            R.string.fund_form_in_periods,
                            pluralStringResource(unit, FundFormUiState.SHORT_PERIODS, FundFormUiState.SHORT_PERIODS)
                        ),
                        short.toMxn()
                    ),
                    SummaryLine(
                        stringResource(
                            R.string.fund_form_in_periods,
                            pluralStringResource(unit, FundFormUiState.LONG_PERIODS, FundFormUiState.LONG_PERIODS)
                        ),
                        long.toMxn(),
                        emphasis = SummaryEmphasis.Bold
                    )
                )
            )
        }
    }
}
