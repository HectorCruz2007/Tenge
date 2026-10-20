package com.zukesito.tenge.data.model

import kotlinx.serialization.Serializable


/** Categoría de gasto **/
@Serializable
data class Category(
    // Expense guarda id, no el nombre
    val id: String,
    val name: String
)