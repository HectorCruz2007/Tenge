package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.datasource.AppStateDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class BalanceRepositoryImpl(
    private val dataSource: AppStateDataSource
) : BalanceRepository {

    override val balanceCents: Flow<Long> =
        dataSource.state.map { it.balanceCents }.distinctUntilChanged()

    override suspend fun adjust(deltaCents: Long) {
        dataSource.update { it.withBalanceChange(deltaCents) }
    }
}