package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.domain.repository.SpaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceMockRepositoryImpl @Inject constructor() : SpaceRepository {
    override fun getAllSpace(): List<SpaceItem> {
        return List(5) {
            SpaceItem(
                name = "$it 번째 공간",
                lockerList = listOf(
                    Locker(Math.random().toString()),
                    Locker(Math.random().toString()),
                    Locker(Math.random().toString()),
                    Locker(Math.random().toString())
                )
            )
        }
    }
}