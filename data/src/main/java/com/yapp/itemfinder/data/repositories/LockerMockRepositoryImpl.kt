package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.locker.AddEditLockerRequest
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerMockRepositoryImpl @Inject constructor() : LockerRepository {

    override suspend fun getLockerById(lockerId: Long): LockerEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun getLockers(spaceId: Long): List<LockerEntity> {
        return listOf(
            LockerEntity(id = 1L, name = "화장대", icon = "ic_container_1", spaceId = 999),
            LockerEntity(id = 2L, name = "냉장고", icon = "ic_container_1", spaceId = 999),
            LockerEntity(id = 3L, name = "옷장", icon = "ic_container_1", spaceId = 999)
        )
    }

    override suspend fun addLocker(
        name: String,
        url: String?,
        spaceId: Long,
        icon: String
    ): LockerEntity {
        return LockerEntity(id = 1L, name = "화장대", icon = "ic_container_1", spaceId = 999)
    }

    override suspend fun editLocker(
        name: String,
        url: String?,
        spaceId: Long,
        icon: String,
        lockerId: Long
    ): LockerEntity {
        return LockerEntity(id = 1L, name = "화장대", icon = "ic_container_1", spaceId = 999)
    }

    override suspend fun deleteLocker(lockerId: Long) = Unit
}
