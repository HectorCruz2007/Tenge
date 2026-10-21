package com.zukesito.tenge.data.datasource

import com.zukesito.tenge.data.model.Category
import com.zukesito.tenge.data.model.SavingsTemplate
import kotlinx.serialization.Serializable

/** Contenido de assets/seed.json */
@Serializable
data class SeedData(
    val categories: List<Category>,
    val templates: List<SavingsTemplate>
)