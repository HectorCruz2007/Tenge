package com.zukesito.tenge.data.datasource

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/** Fuente de los datos seed */
interface SeedDataSource {
    suspend fun load(): SeedData
}

/** Lee los datos seed de un archivo en assets. */
class AssetSeedDataSource(
    context: Context,
    private val fileName: String = "seed.json"
) : SeedDataSource {

    private val assets = context.applicationContext.assets
    private val json = Json { ignoreUnknownKeys = true }

    // Lectura de archivo fuera del hilo principal
    override suspend fun load(): SeedData = withContext(Dispatchers.IO) {
        assets.open(fileName).bufferedReader().use {
            json.decodeFromString(SeedData.serializer(), it.readText())
        }
    }
}