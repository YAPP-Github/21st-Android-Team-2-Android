package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.managespace.ManageSpaceApi
import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.domain.repository.SelectSpaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectSpaceRepositoryImpl @Inject constructor(
    private val manageSpaceApi: ManageSpaceApi
) : SelectSpaceRepository {
    override suspend fun getAllSpaces(): List<SelectSpace> {
        return manageSpaceApi.fetchAllSpaces().spaces.map { it.toSelectSpaceEntity() }
    }
}
