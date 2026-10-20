package com.zukesito.tenge.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow

// Instancia única del DataStore por proceso
private val Context.appDataStore: DataStore<AppState> by dataStore(
    fileName = "tenge_state.json",
    serializer = AppStateSerializer
)

/** Implementación de AppStateDataSource respaldada por DataStore. */
class DataStoreAppStateDataSource(context: Context) : AppStateDataSource {

    private val dataStore = context.applicationContext.appDataStore

    override val state: Flow<AppState> = dataStore.data

    override suspend fun update(transform: (AppState) -> AppState): AppState =
        dataStore.updateData { transform(it) }
}