package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.ManageSpaceItem

interface ManageSpaceRepository {

    suspend fun getAllManageSpaceItems(): List<ManageSpaceItem>

    suspend fun addNewSpace(name: String): ManageSpaceItem

    fun editSpace(): Boolean

    fun deleteSpace(): Boolean
}
