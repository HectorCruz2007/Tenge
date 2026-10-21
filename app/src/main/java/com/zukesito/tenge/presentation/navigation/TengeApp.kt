package com.zukesito.tenge.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zukesito.tenge.R
import com.zukesito.tenge.presentation.components.navigation.BottomNavBar
import com.zukesito.tenge.presentation.components.navigation.BottomNavItem
import com.zukesito.tenge.presentation.feature.expenses.ExpenseFormScreen
import com.zukesito.tenge.presentation.feature.expenses.ExpensesScreen
import com.zukesito.tenge.presentation.feature.home.AdjustBalanceScreen
import com.zukesito.tenge.presentation.feature.home.HomeScreen
import com.zukesito.tenge.presentation.feature.investments.FundFormScreen
import com.zukesito.tenge.presentation.feature.investments.InvestmentsScreen
import com.zukesito.tenge.presentation.feature.recurring.RecurringFormScreen
import com.zukesito.tenge.presentation.feature.recurring.RecurringScreen
import com.zukesito.tenge.presentation.feature.savings.SavingsFormScreen
import com.zukesito.tenge.presentation.feature.savings.SavingsScreen
import com.zukesito.tenge.presentation.theme.TengeIcons
import kotlinx.coroutines.launch

// Secciones de la barra inferior
private data class Tab(val destination: Destination, @param:StringRes val label: Int, val icon: ImageVector)

private val Tabs = listOf(
    Tab(Destination.Home, R.string.nav_home, TengeIcons.Home),
    Tab(Destination.Expenses, R.string.nav_expenses, TengeIcons.Expenses),
    Tab(Destination.Recurring, R.string.nav_recurring, TengeIcons.Recurring),
    Tab(Destination.Savings, R.string.nav_savings, TengeIcons.Savings),
    Tab(Destination.Investments, R.string.nav_investments, TengeIcons.Investments)
)

/** Estructura general: navegación, barra inferior y mensajes de éxito o error. */
@Composable
fun TengeApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val showMessage: (Int) -> Unit = { res ->
        scope.launch { snackbarHostState.showSnackbar(resources.getString(res)) }
    }
    // Al guardar un formulario se regresa y se muestra el mensaje
    val onSaved: (Int) -> Unit = { res ->
        navController.popBackStack()
        showMessage(res)
    }
    val onBack: () -> Unit = { navController.popBackStack() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = Tabs.indexOfFirst { tab ->
        backStackEntry?.destination?.hierarchy?.any { it.hasRoute(tab.destination::class) } == true
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            // Solo se muestra en las secciones, no en los formularios
            if (selectedTab >= 0) {
                BottomNavBar(
                    items = Tabs.map { BottomNavItem(stringResource(it.label), it.icon) },
                    selectedIndex = selectedTab,
                    onSelect = { index -> navController.navigateToTab(Tabs[index].destination) }
                )
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = Destination.Home, modifier = Modifier.padding(padding)) {
            composable<Destination.Home> {
                HomeScreen(
                    onAdjustBalance = { increase -> navController.navigate(Destination.AdjustBalance(increase)) },
                    showMessage = showMessage
                )
            }
            composable<Destination.Expenses> {
                ExpensesScreen(onAddExpense = { navController.navigate(Destination.NewExpense) }, showMessage = showMessage)
            }
            composable<Destination.Recurring> {
                RecurringScreen(onAddRecurring = { navController.navigate(Destination.NewRecurring) }, showMessage = showMessage)
            }
            composable<Destination.Savings> {
                SavingsScreen(onAddPlan = { navController.navigate(Destination.NewSavingsPlan) }, showMessage = showMessage)
            }
            composable<Destination.Investments> {
                InvestmentsScreen(onAddFund = { navController.navigate(Destination.NewFund) }, showMessage = showMessage)
            }
            composable<Destination.AdjustBalance> { AdjustBalanceScreen(onBack = onBack, onSaved = onSaved) }
            composable<Destination.NewExpense> { ExpenseFormScreen(onBack = onBack, onSaved = onSaved) }
            composable<Destination.NewRecurring> { RecurringFormScreen(onBack = onBack, onSaved = onSaved) }
            composable<Destination.NewSavingsPlan> { SavingsFormScreen(onBack = onBack, onSaved = onSaved) }
            composable<Destination.NewFund> { FundFormScreen(onBack = onBack, onSaved = onSaved) }
        }
    }
}

// Cambia de sección conservando el estado de cada una
private fun NavController.navigateToTab(destination: Destination) {
    navigate(destination) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
