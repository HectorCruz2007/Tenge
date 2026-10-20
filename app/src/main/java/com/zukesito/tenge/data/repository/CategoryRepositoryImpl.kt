package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    dataSource: AppStateDataSource
) : CategoryRepository {

    override val categories: Flow<List<Category>> =
        dataSource.state.map { it.categories }.distinctUntilChanged()
}