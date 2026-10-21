package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.Recurring
import com.zukesito.tenge.data.model.RecurringType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

class RecurringRepositoryImpl(
    private val dataSource: AppStateDataSource
) : RecurringRepository {

    override val recurrings: Flow<List<Recurring>> =
        dataSource.state.map { it.recurrings }.distinctUntilChanged()

    override suspend fun add(
        name: String,
        amountCents: Long,
        type: RecurringType,
        frequency: Frequency,
        startDate: LocalDate
    ) {
        val recurring = Recurring(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            amountCents = amountCents,
            type = type,
            frequency = frequency,
            startDate = startDate
        )
        dataSource.update { it.copy(recurrings = it.recurrings + recurring) }
    }

    override suspend fun delete(id: String) {
        dataSource.update { state -> state.copy(recurrings = state.recurrings.filterNot { it.id == id }) }
    }
}
