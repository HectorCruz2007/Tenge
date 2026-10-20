package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.model.Category
import kotlinx.coroutines.flow.Flow

/** Catálogo de categorías de gasto (solo lectura, viene del JSON semilla). */
interface CategoryRepository {
    val categories: Flow<List<Category>>
}