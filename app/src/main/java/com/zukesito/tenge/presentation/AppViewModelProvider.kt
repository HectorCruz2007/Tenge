package com.zukesito.tenge.presentation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.zukesito.tenge.TengeApplication
import com.zukesito.tenge.core.di.AppContainer
import com.zukesito.tenge.presentation.feature.expenses.ExpenseFormViewModel
import com.zukesito.tenge.presentation.feature.expenses.ExpensesViewModel
import com.zukesito.tenge.presentation.feature.home.AdjustBalanceViewModel
import com.zukesito.tenge.presentation.feature.home.HomeViewModel
import com.zukesito.tenge.presentation.feature.investments.FundFormViewModel
import com.zukesito.tenge.presentation.feature.investments.InvestmentsViewModel
import com.zukesito.tenge.presentation.feature.recurring.RecurringFormViewModel
import com.zukesito.tenge.presentation.feature.recurring.RecurringViewModel
import com.zukesito.tenge.presentation.feature.savings.SavingsFormViewModel
import com.zukesito.tenge.presentation.feature.savings.SavingsViewModel

/** Crea los ViewModels inyectándoles repositorios del contenedor */
object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val c = container()
            HomeViewModel(
                c.balanceRepository, c.expenseRepository, c.recurringRepository,
                c.savingsRepository, c.investmentRepository, c.noticeRepository
            )
        }
        initializer { AdjustBalanceViewModel(createSavedStateHandle(), container().balanceRepository) }
        initializer {
            val c = container()
            ExpensesViewModel(
                c.balanceRepository, c.expenseRepository, c.categoryRepository,
                c.recurringRepository, c.savingsRepository
            )
        }
        initializer {
            val c = container()
            ExpenseFormViewModel(
                c.expenseRepository, c.categoryRepository, c.balanceRepository,
                c.recurringRepository, c.savingsRepository
            )
        }
        initializer { RecurringViewModel(container().recurringRepository) }
        initializer { RecurringFormViewModel(container().recurringRepository) }
        initializer { SavingsViewModel(container().savingsRepository) }
        initializer { SavingsFormViewModel(container().savingsRepository) }
        initializer { InvestmentsViewModel(container().investmentRepository) }
        initializer { FundFormViewModel(container().investmentRepository) }
    }
}

// El contenedor vive en TengeApplication
private fun CreationExtras.container(): AppContainer =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TengeApplication).container
