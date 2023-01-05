package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.domain.repository.SpaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceMockRepositoryImpl @Inject constructor() : SpaceRepository {
    override fun getAllSpace(): List<SpaceItem> {
        return listOf(
            SpaceItem(
                name = "1 번째 공간",
                lockerList = listOf(
                    Locker(id = 1L, name = "화장대"),
                )
            ),
            SpaceItem(
                name = "2 번째 공간",
                lockerList = listOf(
                    Locker(id = 2L, name = "화장대"),
                    Locker(id = 3L, name = "냉장고"),
                )
            ),
            SpaceItem(
                name = "3 번째 공간",
                lockerList = listOf(
                    Locker(id = 4L, name = "화장대"),
                    Locker(id = 5L, name = "냉장고"),
                    Locker(id = 6L, name = "옷장"),
                )
            ),
            SpaceItem(
                name = "4 번째 공간",
                lockerList = listOf(
                    Locker(id = 7L, name = "화장대"),
                    Locker(id = 8L, name = "냉장고"),
                    Locker(id = 9L, name = "옷장"),
                    Locker(id = 10L, name = "옷장"),
                )
            ),
            SpaceItem(
                name = "$5 번째 공간",
                lockerList = listOf(
                    Locker(id = 11L, Math.random().toString()),
                    Locker(id = 12L, Math.random().toString()),
                    Locker(id = 13L, Math.random().toString()),
                    Locker(id = 14L, Math.random().toString())
                )
            )
        )
    }
}
