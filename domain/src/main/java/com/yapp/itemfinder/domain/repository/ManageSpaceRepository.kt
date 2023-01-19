package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.ManageSpaceEntity

interface ManageSpaceRepository {

    suspend fun getAllManageSpaceItems(): List<ManageSpaceEntity>

    suspend fun addNewSpace(name: String): ManageSpaceEntity

    fun editSpace(): Boolean

    fun deleteSpace(): Boolean
}
