package com.zukesito.tenge.data.datasource

import com.zukesito.tenge.data.model.BalanceAdjustment
import com.zukesito.tenge.data.model.Category
import com.zukesito.tenge.data.model.Expense
import com.zukesito.tenge.data.model.InvestmentFund
import com.zukesito.tenge.data.model.Recurring
import com.zukesito.tenge.data.model.SavingsPlan
import com.zukesito.tenge.data.model.SavingsTemplate
import kotlinx.serialization.Serializable

/**
 * Estado completo de la app guardado en DataStore.
 */
@Serializable
data class AppState(
    // Saldo disponible en centavos
    val balanceCents: Long = 0L,
    // Historial de ajustes manuales del saldo
    val adjustments: List<BalanceAdjustment> = emptyList(),
    val expenses: List<Expense> = emptyList(),
    val recurrings: List<Recurring> = emptyList(),
    val savingsPlans: List<SavingsPlan> = emptyList(),
    val funds: List<InvestmentFund> = emptyList(),
    // Catálogos cargados del JSON semilla
    val categories: List<Category> = emptyList(),
    val templates: List<SavingsTemplate> = emptyList(),
    // true cuando ya se cargaron los datos semilla
    val isSeeded: Boolean = false
)