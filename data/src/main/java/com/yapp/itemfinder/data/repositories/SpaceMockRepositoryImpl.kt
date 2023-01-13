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
                    Locker(id = 1L, name = "화장대", com.yapp.itemfinder.domain.R.drawable.box),
                )
            ),
            SpaceItem(
                name = "2 번째 공간",
                lockerList = listOf(
                    Locker(id = 2L, name = "화장대", com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 3L, name = "냉장고", com.yapp.itemfinder.domain.R.drawable.box),
                )
            ),
            SpaceItem(
                name = "3 번째 공간",
                lockerList = listOf(
                    Locker(id = 4L, name = "화장대", com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 5L, name = "냉장고", com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 6L, name = "옷장", com.yapp.itemfinder.domain.R.drawable.box),
                )
            ),
            SpaceItem(
                name = "4 번째 공간",
                lockerList = listOf(
                    Locker(id = 7L, name = "화장대", com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 8L, name = "냉장고", com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 9L, name = "옷장", com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 10L, name = "옷장", com.yapp.itemfinder.domain.R.drawable.box),
                )
            ),
            SpaceItem(
                name = "5 번째 공간",
                lockerList = listOf(
                    Locker(id = 1L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 2L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 3L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 4L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 5L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box)
                )
            ),
            SpaceItem(
                name = "6 번째 공간",
                lockerList = listOf(
                    Locker(id = 1L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 2L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 3L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                )
            ),
            SpaceItem(
                name = "7 번째 공간",
                lockerList = listOf(
                    Locker(id = 1L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                    Locker(id = 2L,Math.random().toString(), com.yapp.itemfinder.domain.R.drawable.box),
                )
            )
        )
    }
}
