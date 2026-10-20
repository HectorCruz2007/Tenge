package com.zukesito.tenge.data.datasource

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/** Convierte AppState a JSON para DataStore. */
object AppStateSerializer : Serializer<AppState> {

    // Ignora campos desconocidos para tolerar cambios futuros del esquema
    private val json = Json { ignoreUnknownKeys = true }

    override val defaultValue: AppState = AppState()

    override suspend fun readFrom(input: InputStream): AppState =
        try {
            json.decodeFromString(AppState.serializer(), input.readBytes().decodeToString())
        } catch (e: SerializationException) {
            throw CorruptionException("No se pudo leer el estado guardado", e)
        }

    override suspend fun writeTo(t: AppState, output: OutputStream) =
        output.write(json.encodeToString(AppState.serializer(), t).encodeToByteArray())
}