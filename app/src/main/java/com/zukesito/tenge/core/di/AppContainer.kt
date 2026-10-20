package com.zukesito.tenge.core.di

import com.zukesito.tenge.data.repository.BalanceRepository
import com.zukesito.tenge.data.repository.CategoryRepository
import com.zukesito.tenge.data.repository.ExpenseRepository

/** Dependencias compartidas; los ViewModels reciben abstracciones de aquí */
interface AppContainer {
    val balanceRepository: BalanceRepository
    val expenseRepository: ExpenseRepository
    val categoryRepository: CategoryRepository
}