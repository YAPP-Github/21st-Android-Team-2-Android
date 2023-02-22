package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.ManageSpaceEntity

interface ManageSpaceRepository {

    suspend fun getAllManageSpaceItems(): List<ManageSpaceEntity>

    suspend fun addNewSpace(name: String): ManageSpaceEntity

    suspend fun editSpace(name: String, spaceId: Long): ManageSpaceEntity

    suspend fun deleteSpace(spaceId: Long): Boolean
}
