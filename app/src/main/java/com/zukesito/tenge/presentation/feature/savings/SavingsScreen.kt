package com.zukesito.tenge.presentation.feature.savings

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.SavingsStatus
import com.zukesito.tenge.core.finance.toMxDate
import com.zukesito.tenge.core.finance.toMxn
import com.zukesito.tenge.presentation.AppViewModelProvider
import com.zukesito.tenge.presentation.components.buttons.AddButton
import com.zukesito.tenge.presentation.components.cards.SavingsPlanCard
import com.zukesito.tenge.presentation.components.cards.SavingsPlanDetails
import com.zukesito.tenge.presentation.components.feedback.ConfirmDeleteDialog
import com.zukesito.tenge.presentation.components.feedback.EmptyPlaceholder
import com.zukesito.tenge.presentation.components.indicators.BadgeStyle
import com.zukesito.tenge.presentation.components.templates.TabScreenTemplate
import com.zukesito.tenge.presentation.labels.labelRes
import com.zukesito.tenge.presentation.labels.perPeriodRes

/** Planes de ahorro con su avance, cuota y fechas. */
@Composable
fun SavingsScreen(
    onAddPlan: () -> Unit,
    showMessage: (Int) -> Unit,
    viewModel: SavingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.message) {
        state.message?.let {
            showMessage(it)
            viewModel.onMessageShown()
        }
    }
    SavingsContent(state, onAddPlan, viewModel::onRequestDelete)
    state.pendingDelete?.let {
        ConfirmDeleteDialog(
            title = stringResource(R.string.delete_plan_title),
            message = stringResource(R.string.delete_plan_message, it.name),
            onConfirm = viewModel::onConfirmDelete,
            onDismiss = viewModel::onDismissDelete
        )
    }
}

@Composable
private fun SavingsContent(
    state: SavingsUiState,
    onAddPlan: () -> Unit,
    onRequestDelete: (SavingsPlanItem) -> Unit
) {
    TabScreenTemplate(
        title = stringResource(R.string.savings_title),
        // Sin planes, la acción principal está en el centro de la pantalla
        floatingButton = if (state.plans.isEmpty()) null else {
            { AddButton(stringResource(R.string.savings_add), onAddPlan) }
        }
    ) {
        if (state.plans.isEmpty()) {
            item {
                EmptyPlaceholder(
                    title = stringResource(R.string.savings_empty_title),
                    message = stringResource(R.string.savings_empty_message),
                    actionLabel = stringResource(R.string.savings_form_save),
                    onAction = onAddPlan,
                    modifier = Modifier.fillParentMaxHeight(0.85f)
                )
            }
        }
        items(state.plans, key = { it.id }) { plan ->
            SavingsPlanCard(
                name = plan.name,
                statusLabel = stringResource(plan.status.labelRes()),
                statusStyle = when (plan.status) {
                    SavingsStatus.IN_PROGRESS -> BadgeStyle.Soft
                    SavingsStatus.BEHIND -> BadgeStyle.Outlined
                    SavingsStatus.COMPLETED -> BadgeStyle.Strong
                },
                accumulatedCents = plan.accumulatedCents,
                goalCents = plan.goalCents,
                progress = plan.progress,
                details = planDetails(plan),
                onLongClick = { onRequestDelete(plan) }
            )
        }
    }
}

// Detalle de cuota y fechas; los planes cumplidos no lo muestran
@Composable
private fun planDetails(plan: SavingsPlanItem): SavingsPlanDetails? {
    val nextDue = plan.nextDueDate ?: return null
    val estimated = plan.estimatedDate ?: return null
    return SavingsPlanDetails(
        quotaLabel = stringResource(plan.termUnit.perPeriodRes(), plan.quotaCents.toMxn()),
        nextDueLabel = nextDue.toMxDate(),
        estimatedLabel = estimated.toMxDate(),
        deadlineLabel = plan.deadline.toMxDate()
    )
}
