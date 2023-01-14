package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.home.HomeSpaceApi
import com.yapp.itemfinder.domain.coroutines.DispatcherProvider
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.domain.repository.HomeSpaceRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeSpaceRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val homeSpaceApi: HomeSpaceApi
) : HomeSpaceRepository {
    override suspend fun getHomeSpaces(): List<SpaceItem> =
        withContext(dispatcherProvider.io) {
            val response = homeSpaceApi.fetchHomeSpaces()
            val list = response.body() ?: listOf()
            val result = list.map { it.refineToSpaceItem() }
            result
        }

}
