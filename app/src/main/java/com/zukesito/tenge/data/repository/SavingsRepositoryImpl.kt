package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.SavingsPlan
import com.zukesito.tenge.data.model.SavingsTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

class SavingsRepositoryImpl(
    private val dataSource: AppStateDataSource
) : SavingsRepository {

    override val plans: Flow<List<SavingsPlan>> =
        dataSource.state.map { it.savingsPlans }.distinctUntilChanged()

    override val templates: Flow<List<SavingsTemplate>> =
        dataSource.state.map { it.templates }.distinctUntilChanged()

    override suspend fun add(
        name: String,
        goalCents: Long,
        termCount: Int,
        termUnit: Frequency,
        startDate: LocalDate
    ) {
        val plan = SavingsPlan(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            goalCents = goalCents,
            termCount = termCount,
            termUnit = termUnit,
            startDate = startDate
        )
        dataSource.update { it.copy(savingsPlans = it.savingsPlans + plan) }
    }

    override suspend fun delete(id: String) {
        dataSource.update { state -> state.copy(savingsPlans = state.savingsPlans.filterNot { it.id == id }) }
    }
}
