package com.zukesito.tenge.core.di

import android.content.Context
import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.datasource.DataStoreAppStateDataSource
import com.zukesito.tenge.data.repository.BalanceRepository
import com.zukesito.tenge.data.repository.BalanceRepositoryImpl
import com.zukesito.tenge.data.repository.CategoryRepository
import com.zukesito.tenge.data.repository.CategoryRepositoryImpl
import com.zukesito.tenge.data.repository.ExpenseRepository
import com.zukesito.tenge.data.repository.ExpenseRepositoryImpl
import com.zukesito.tenge.data.repository.SeedRepository
import com.zukesito.tenge.data.datasource.AssetSeedDataSource
import com.zukesito.tenge.data.repository.SeedRepositoryImpl

/** Implementación real del contenedor*/
class DefaultAppContainer(private val context: Context) : AppContainer {

    // Una sola fuente de datos para toda la app
    private val dataSource: AppStateDataSource = DataStoreAppStateDataSource(context)

    // cada repositorio se crea la primera vez que se usa
    override val balanceRepository: BalanceRepository by lazy { BalanceRepositoryImpl(dataSource) }
    override val expenseRepository: ExpenseRepository by lazy { ExpenseRepositoryImpl(dataSource) }
    override val categoryRepository: CategoryRepository by lazy { CategoryRepositoryImpl(dataSource) }
    override val seedRepository: SeedRepository by lazy {
        SeedRepositoryImpl(dataSource, AssetSeedDataSource(context))
    }
}