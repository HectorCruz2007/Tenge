package com.zukesito.tenge.presentation.navigation

import kotlinx.serialization.Serializable

/** Rutas de la app. */
sealed interface Destination {
    // Secciones de la barra inferior
    @Serializable data object Home : Destination
    @Serializable data object Expenses : Destination
    @Serializable data object Recurring : Destination
    @Serializable data object Savings : Destination
    @Serializable data object Investments : Destination

    // Formularios
    @Serializable data class AdjustBalance(val increase: Boolean) : Destination
    @Serializable data object NewExpense : Destination
    @Serializable data object NewRecurring : Destination
    @Serializable data object NewSavingsPlan : Destination
    @Serializable data object NewFund : Destination
}
