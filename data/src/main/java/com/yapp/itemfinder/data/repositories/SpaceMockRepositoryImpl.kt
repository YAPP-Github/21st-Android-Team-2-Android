package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.domain.repository.SpaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceMockRepositoryImpl @Inject constructor() : SpaceRepository {
    override suspend fun getAllSpaces(): List<SelectSpace> {
        return listOf(
            SelectSpace(id = 1, name = "서재"),
            SelectSpace(id = 2, name = "주방"),
            SelectSpace(id = 3, name = "작업실"),
            SelectSpace(id = 4, name = "베란다"),
            SelectSpace(id = 5, name = "드레스룸")
        )
    }

    override suspend fun getSpaceById(spaceId: Long): ManageSpaceEntity {
        return ManageSpaceEntity(name = "")
    }
}
