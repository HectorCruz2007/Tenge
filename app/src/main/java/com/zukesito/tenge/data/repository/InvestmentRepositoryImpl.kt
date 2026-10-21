package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.InvestmentFund
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

class InvestmentRepositoryImpl(
    private val dataSource: AppStateDataSource
) : InvestmentRepository {

    override val funds: Flow<List<InvestmentFund>> =
        dataSource.state.map { it.funds }.distinctUntilChanged()

    override suspend fun add(
        name: String,
        amountCents: Long,
        ratePercent: Double,
        frequency: Frequency,
        registeredAt: LocalDate
    ) {
        val fund = InvestmentFund(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            amountCents = amountCents,
            ratePercent = ratePercent,
            frequency = frequency,
            registeredAt = registeredAt
        )
        dataSource.update { it.copy(funds = it.funds + fund) }
    }

    override suspend fun delete(id: String) {
        dataSource.update { state -> state.copy(funds = state.funds.filterNot { it.id == id }) }
    }
}
