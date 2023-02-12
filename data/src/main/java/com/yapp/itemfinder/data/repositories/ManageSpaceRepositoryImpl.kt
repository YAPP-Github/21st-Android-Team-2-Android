package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.managespace.AddSpaceRequest
import com.yapp.itemfinder.data.network.api.managespace.ManageSpaceApi
import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.domain.repository.ManageSpaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageSpaceRepositoryImpl @Inject constructor(
    private val manageSpaceApi: ManageSpaceApi
) : ManageSpaceRepository {

    override suspend fun getAllManageSpaceItems(): List<ManageSpaceEntity> =
        manageSpaceApi.fetchAllSpaces().spaces.map { it.toManageSpaceEntity() }

    override suspend fun addNewSpace(name: String): ManageSpaceEntity =
        manageSpaceApi.addNewSpace(AddSpaceRequest(name)).toManageSpaceEntity()

    override suspend fun editSpace(name: String, spaceId: Long): ManageSpaceEntity =
        manageSpaceApi.editSpace(spaceId = spaceId, editSpaceRequest = AddSpaceRequest(name))
            .toManageSpaceEntity()


    override fun deleteSpace(): Boolean {
        // api call
        return true
    }
}
