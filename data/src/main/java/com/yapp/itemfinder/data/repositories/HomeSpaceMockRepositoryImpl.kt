package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.Locker

import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.domain.repository.HomeSpaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeSpaceMockRepositoryImpl @Inject constructor() : HomeSpaceRepository {

    override suspend fun getHomeSpaces(): List<SpaceItem> {
        return listOf(
            SpaceItem(
                id = 1,
                name = "1 번째 공간",
                lockerList = listOf(
                    Locker(spaceId = 1, id = 1L, name = "화장대", icon = "box"),
                )
            ),
            SpaceItem(
                id = 2,
                name = "2 번째 공간",
                lockerList = listOf(
                    Locker(spaceId = 2, id = 2L, name = "화장대", icon = "box"),
                    Locker(spaceId = 2, id = 3L, name = "냉장고", icon = "box"),
                )
            ),
            SpaceItem(
                id = 3,
                name = "3 번째 공간",
                lockerList = listOf(
                    Locker(spaceId = 3, id = 4L, name = "화장대", icon = "box"),
                    Locker(spaceId = 3, id = 5L, name = "냉장고", icon = "box"),
                    Locker(spaceId = 3, id = 6L, name = "옷장", icon = "box"),
                )
            ),
            SpaceItem(
                id = 4,
                name = "4 번째 공간",
                lockerList = listOf(
                    Locker(spaceId = 4, id = 7L, name = "화장대", icon = "box"),
                    Locker(spaceId = 4, id = 8L, name = "냉장고", icon = "box"),
                    Locker(spaceId = 4, id = 9L, name = "옷장", icon = "box"),
                    Locker(spaceId = 4, id = 10L, name = "옷장", icon = "box"),
                )
            ),
            SpaceItem(
                id = 5,
                name = "5 번째 공간",
                lockerList = listOf(
                    Locker(
                        spaceId = 5,
                        name = "냉장고",
                        id = 1L,
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 5,
                        name = "냉장고",
                        id = 2L,
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 5,
                        name = "냉장고",
                        id = 3L,
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 5,
                        name = "냉장고",
                        id = 4L,
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 5,
                        name = "냉장고",
                        id = 4L,
                        icon = "box"
                    )
                )
            ),
            SpaceItem(
                id = 6,
                name = "6 번째 공간",
                lockerList = listOf(
                    Locker(
                        spaceId = 6,
                        name = "6locker",
                        id = 1L,
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 6,
                        name = "6locker",
                        id = 2L,
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 6,
                        name = "6locker",
                        id = 3L,
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 6,
                        name = "6locker",
                        id = 4L,
                        icon = "box"
                    )
                )
            ),
            SpaceItem(
                id = 7,
                name = "7 번째 공간",
                lockerList = listOf(
                    Locker(
                        spaceId = 7,
                        id = 1L,
                        name = "보관함 mock",
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 7,
                        id = 2L,
                        name = "보관함 mock",
                        icon = "box"
                    ),
                    Locker(
                        spaceId = 7,
                        id = 3L,
                        name = "보관함 mock",
                        icon = "box"
                    ),

                    )
            )
        )
    }
}
