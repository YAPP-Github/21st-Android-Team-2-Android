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
                name = "$0 번째 공간",
                lockerList = listOf(
                )
            ),
            SpaceItem(
                name = "$1 번째 공간",
                lockerList = listOf(
                    Locker(name = "화장대", "https://picsum.photos/200"),
                )
            ),
            SpaceItem(
                name = "$2 번째 공간",
                lockerList = listOf(
                    Locker(name = "화장대", "https://picsum.photos/200"),
                    Locker(name = "냉장고", "https://picsum.photos/200"),
                )
            ),
            SpaceItem(
                name = "$3 번째 공간",
                lockerList = listOf(
                    Locker(name = "화장대", "https://picsum.photos/200"),
                    Locker(name = "냉장고", "https://picsum.photos/200"),
                    Locker(name = "옷장", "https://picsum.photos/200"),
                )
            ),
            SpaceItem(
                name = "$4 번째 공간",
                lockerList = listOf(
                    Locker(name = "화장대", "https://picsum.photos/200"),
                    Locker(name = "냉장고", "https://picsum.photos/200"),
                    Locker(name = "옷장", "https://picsum.photos/200"),
                    Locker(name = "옷장", "https://picsum.photos/200"),
                )
            ),
            SpaceItem(
                name = "$5 번째 공간",
                lockerList = listOf(
                    Locker(name = "화장대", "https://picsum.photos/200"),
                    Locker(name = "냉장고", "https://picsum.photos/200"),
                    Locker(name = "옷장", "https://picsum.photos/200"),
                    Locker(name = "옷장", "https://picsum.photos/200"),
                    Locker(name = "화장대", "https://picsum.photos/200"),
                )
            ),
        )
    }
}