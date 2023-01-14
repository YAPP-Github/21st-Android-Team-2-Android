package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.repository.LockerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerMockRepositoryImpl @Inject constructor() : LockerRepository {
    override suspend fun getLockers(spaceId: Long): List<Locker> {
        return listOf(
            Locker(id = 1L, name = "화장대", icon = "ic_container_1", spaceId = 999),
            Locker(id = 2L, name = "냉장고", icon = "ic_container_1", spaceId = 999),
            Locker(id = 3L, name = "옷장", icon = "ic_container_1", spaceId = 999)
        )
    }
}
