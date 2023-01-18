package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.domain.repository.SelectSpaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectSpaceMockRepositoryImpl @Inject constructor() : SelectSpaceRepository {
    override suspend fun getAllSpaces(): List<SelectSpace> {
        return listOf(
            SelectSpace(id = 1, name = "서재"),
            SelectSpace(id = 2, name = "주방", isChecked = true),
            SelectSpace(id = 3, name = "작업실"),
            SelectSpace(id = 4, name = "베란다"),
            SelectSpace(id = 5, name = "드레스룸")
        )
    }
}
